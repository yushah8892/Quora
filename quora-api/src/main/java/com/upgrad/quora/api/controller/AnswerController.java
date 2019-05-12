package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

//@RestController annotation combines @Controller and @ResponseBody annotations. This will help in labeling the class
//as a controller class as well as convert the return values of all the methods into JSON and pass it to the response models.
@RestController
//@RequestMapping is used to map the web requests to the specific classes/methods.
@RequestMapping("/")
public class AnswerController {

    //Injecting the dependency using @Autowired annotation
    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    //@RequestMapping is used to map the web requests to the specific classes/methods.
    //The HTTP method used here is POST since we are creating an answer.
    //This method will accept the request in JSON format and give the response in JSON format.
    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") String questionId, @RequestParam("authorization") String accessToken, AnswerRequest answerRequest)
            throws InvalidQuestionException, AuthorizationFailedException {

        final AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAns(answerRequest.getAnswer());
        answerEntity.setDate(ZonedDateTime.now());
        AnswerEntity postedAnswer = answerService.createAnswer(accessToken,answerEntity,questionId);
        AnswerResponse answerResponse =new AnswerResponse().id(postedAnswer.getUuid()).status("ANSWER CREATED");

        //The method will return the response with the HTTP status as created.
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    //@RequestMapping is used to map the web requests to the specific classes/methods.
    //The HTTP method used here in PUT which in CRUD operations is update.
    //This method will accept the request in JSON format and give the response in JSON format.
    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(@PathVariable("answerId") String answerId, @RequestParam("authorization") String accessToken, AnswerEditRequest answerEditRequest)
            throws AuthorizationFailedException, AnswerNotFoundException {

        AnswerEntity editedAnswer = answerService.editAnswerContent(accessToken, answerId,answerEditRequest.getContent());

        AnswerEditResponse editedAnswerResponse = new AnswerEditResponse().id(editedAnswer.getUuid()).status("ANSWER EDITED");

        //The method will return the response with the HTTP status as accepted.
        return new ResponseEntity<AnswerEditResponse>(editedAnswerResponse, HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/answer/all/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>>  getAllAnswer(@PathVariable("questionId") String questionId,@RequestParam("authorization") String accessToken) throws AuthorizationFailedException, InvalidQuestionException {

        final  List<AnswerEntity> allAnswer = answerService.getAllAnswer(accessToken, questionId);
        Iterator<AnswerEntity> iterator = allAnswer.iterator();
        List<AnswerDetailsResponse> answerDeleteResponsesList = new LinkedList<>();
        while(iterator.hasNext()){
            AnswerEntity answerEntity = iterator.next();
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse().id(answerEntity.getUuid()).questionContent(answerEntity.getQuestionEntity().getContent()).answerContent(answerEntity.getAns());
            answerDeleteResponsesList.add(answerDetailsResponse);
        }

        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDeleteResponsesList,HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestParam("authorization") String accessToken, @PathVariable("answerId") String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = answerService.deleteAnswer(accessToken, answerId);

        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.ACCEPTED);
    }
}