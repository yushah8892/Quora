package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RestController

public class AnswerController {

    @Autowired
    private AnswerService answerService;


    @Autowired
    private AnswerEntity answerEntity;


    @RequestMapping(method = RequestMethod.POST, path = "answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestParam("authorization") String accessToken, @PathVariable("answerId") String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = answerService.deleteAnswer(accessToken, answerId);

        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswers(@RequestParam("authorization") String accessToken,@RequestParam("questionId") String questionId) throws AuthorizationFailedException, UserNotFoundException {
        String userId = new AnswerDetailsResponse().id(answerEntity.getUuid()).toString();


        final List<AnswerEntity> allAnswers = answerService.getAnswerByQuestion(accessToken, userId, questionId);
        final Iterator<AnswerEntity> iterator = allAnswers.iterator();
        List<AnswerDetailsResponse> answerDetailsResponseList = new LinkedList<>();
        while (iterator.hasNext()) {

            AnswerEntity answerEntity = iterator.next();
            AnswerDetailsResponse answerDetailsResponse =new AnswerDetailsResponse().id(answerEntity.getUuid()).questionContent(answerEntity.getQuestionEntity().getContent()).answerContent(answerEntity.getAns());
            answerDetailsResponseList.add(answerDetailsResponse);

        }
        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponseList, HttpStatus.ACCEPTED);
    }
}