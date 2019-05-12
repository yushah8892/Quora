package com.upgrad.quora.service.dao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.business.AnswerService;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository

public class AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {

        entityManager.remove(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerById(String answerId){
        try {
            return entityManager.createNamedQuery("getAnswerById",AnswerEntity.class).setParameter("uuid",answerId).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public List<AnswerEntity> getAnswerByQuestion(QuestionEntity questionId){
        List<AnswerEntity> allAnswers= entityManager.createNamedQuery("getAnswerByQuestion",AnswerEntity.class).setParameter("question",questionId).getResultList();
        return allAnswers;
    }

}
