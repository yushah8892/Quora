package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity){

        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestion(){


        List<QuestionEntity> allQuestions = entityManager.createNamedQuery("getAllQuestion", QuestionEntity.class).getResultList();
        return allQuestions;
    }
    public  QuestionEntity getQuestionById(String questionId){
        try {
            return entityManager.createNamedQuery("getQuestionById",QuestionEntity.class).setParameter("uuid",questionId).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public QuestionEntity editQuestion(QuestionEntity questionEntity){
        entityManager.merge(questionEntity);
        return questionEntity;
    }
}
