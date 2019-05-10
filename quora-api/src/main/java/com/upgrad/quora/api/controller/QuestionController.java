package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import javax.ws.rs.PathParam;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.POST,path = "/question/create",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<QuestionResponse> createQUestion(@RequestParam("authorization") String accessToken, QuestionRequest questionRequest) throws AuthorizationFailedException {
        final QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setDate(ZonedDateTime.now());
        QuestionEntity postedQuestion = questionService.createQuestion(accessToken, questionEntity);

        QuestionResponse questionResponse = new QuestionResponse().id(postedQuestion.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);


    }

    @RequestMapping(method = RequestMethod.GET,path = "/question/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDeleteResponse>> getAllQuestions(@RequestParam("authorization") String accessToken) throws AuthorizationFailedException {
        List<QuestionEntity> allQuestions = questionService.getAllQuestions(accessToken);
      //  if(allQuestions != null && allQuestions.size() > 0){

            final Iterator<QuestionEntity> iterator = allQuestions.iterator();
            List<QuestionDeleteResponse> questionResponseList = new LinkedList<>();
            while(iterator.hasNext()){
                QuestionEntity questionEntity = iterator.next();
                QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid()).status(questionEntity.getContent());
                questionResponseList.add(questionDeleteResponse);
            }
           return new ResponseEntity<List<QuestionDeleteResponse>>(questionResponseList,HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.POST,path = "question/edit/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(QuestionEditRequest questionEditRequest, @RequestParam("authorization")String accessToken, @PathVariable("questionId")String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionService.editQuestion(accessToken, questionId, questionEditRequest.getContent());

        QuestionEditResponse questionEditResponse  = new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.CREATED);

    }
}
