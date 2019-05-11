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

//@Service annotation labels a class as a service class
@Service
public class AnswerService {

    //Injecting the dependency using @Autowired annotation
    @Autowired
    private AnswerDao answerDao;

    //Injecting the dependency using @Autowired annotation
    @Autowired
    private UserDao userDao;


    //@Transactional annotation is used to do all the transactions related to the data base. It simplifies the process of
    //transacting with a database.
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

    public AnswerEntity editAnswerContent(String accessToken, String answerId, String content)
            throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);

        if(userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to edit an answer");
        } else {
            AnswerEntity editAnswer = answerDao.getAnswerById(answerId);
            if(editAnswer == null) {
                throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
            } else {
                if (editAnswer.getUserEntity().getId() != userAuthTokenEntity.getUser().getId()){
                    throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
                } else {
                    editAnswer.setAnswer(content);
                    return answerDao.editAnswerContent(editAnswer);
                }
            }
        }
    }
}