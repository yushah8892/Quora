package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(String accessToken, AnswerEntity answerEntity) throws
            InvalidQuestionException, AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);
        String questionUUID = new QuestionEntity().getUuid();

        if (questionUUID == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        } else if(userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        } else if(userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        } else {
            answerEntity.setUserEntity(userAuthTokenEntity.getUser());
            return answerDao.createAnswer(answerEntity);
        }

    }

    public AnswerEntity editAnswerContent(String accessToken, AnswerEntity editedAnswerEntity)
            throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);
        String answerUUID = new AnswerEntity().getUuid();

        if(userAuthTokenEntity.getUser() != editedAnswerEntity.getUserEntity()){
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        } else if (userAuthTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        } else if(userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        } else if (answerUUID == null){
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        } else {
            return answerDao.editAnswerContent(editedAnswerEntity);
        }

    }
}
