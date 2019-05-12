package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;


//@Service annotation labels a class as a service class
@Service
public class AnswerService {

    //Injecting the dependency using @Autowired annotation
    @Autowired
    private AnswerDao answerDao;

    //Injecting the dependency using @Autowired annotation
    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    private Logger logger = LoggerFactory.getLogger(AnswerService.class);

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(String accessToken, String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);
        logger.info("acceessToken:" + accessToken);
        logger.info("answerId:" + answerId);
        if (userAuthTokenEntity == null) {
            logger.info("inside auth token is null");
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userAuthTokenEntity.getLogoutAt() != null) {
            logger.info("inside user has logout");
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete the answer");
        } else {
            AnswerEntity retreivedAnswer = answerDao.getAnswerById(answerId);
            if (retreivedAnswer == null) {
                throw new AnswerNotFoundException("ANS-001", "Answer does not exist");
            }
            if (userAuthTokenEntity.getUser().getRole() == "admin") {
                return answerDao.deleteAnswer(retreivedAnswer);
            } else if (retreivedAnswer.getUser().getId() == userAuthTokenEntity.getUser().getId()) {
                return answerDao.deleteAnswer(retreivedAnswer);

            } else {
                throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
            }

        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAnswerByQuestion(String accessToken, String userId, String questionId) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);
        List<QuestionEntity> allQuestionByUser = null;
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all answers posted by a specific user");

        } else {

            //UserEntity userDetails = userDao.getUserDetails(userId);
            String userDetails=userAuthTokenEntity.getUuid();
            if (userDetails == null) {
                throw new UserNotFoundException("USR-001", "User with entered uuid whose answer details are to be seen does not exist");

            } else {
                QuestionEntity questionEntity = questionDao.getQuestionById(questionId);

                return answerDao.getAnswerByQuestion(questionEntity);
            }
        }
    }
}

    //@Transactional annotation is used to do all the transactions related to the data base. It simplifies the process of
    //transacting with a database.
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(String accessToken, AnswerEntity answerEntity, String questionId) throws
            InvalidQuestionException, AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);


        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        } else if(userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        } else if(userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        } else {
            answerEntity.setUserEntity(userAuthTokenEntity.getUser());
            answerEntity.setQuestionEntity(questionEntity);
            return answerDao.createAnswer(answerEntity);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
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


    @Transactional(propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAllAnswer(String accessToken,String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);

        if(userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the answers");
        }else {
            QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
            if (questionEntity == null) {
                throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
            }else{

               return answerDao.getAllAnswer(questionEntity);

            }

        }
    }
}
