package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    private Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(String accessToken,QuestionEntity questionEntity) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);
        if(userAuthTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }else if(userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }else{
            questionEntity.setUser(userAuthTokenEntity.getUser());
            return questionDao.createQuestion(questionEntity);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(String accessToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);
        if(userAuthTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }else if(userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }else{
            return questionDao.getAllQuestion();
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(String accessToken,String questionId,String content) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);
        logger.info("acceessToken:" + accessToken);
        logger.info("questionId:" + questionId);
        logger.info("content:"+ content);
        if(userAuthTokenEntity == null){
            logger.info("inside auth token is null");
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");

        }else if(userAuthTokenEntity.getLogoutAt() != null) {
            logger.info("inside user has logout");
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");

        }else {
            logger.info("inside retrive user question");
            QuestionEntity retrivedQuestion = questionDao.getQuestionById(questionId);
            if (retrivedQuestion == null) {
                throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
            } else {
                if (retrivedQuestion.getUser().getId() != userAuthTokenEntity.getUser().getId()) {
                    throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
                } else {
                    retrivedQuestion.setContent(content);
                    return questionDao.editQuestion(retrivedQuestion);
                }
            }
        }
    }
}
