package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

//@Repository annotation marks the class as a Data Access Object
@Repository
public class AnswerDao {

    //A persistence context handles a set of entities which hold data to be persisted in some persistence store
    @PersistenceContext
    //Entity Manager manages the entities using persistence context
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }


    public AnswerEntity editAnswerContent(AnswerEntity editedAnswerEntity) {
        entityManager.merge(editedAnswerEntity);
        return editedAnswerEntity;
    }

    public AnswerEntity getAnswerById(String answerId) {
        try {
            return entityManager.createNamedQuery("getAnswerById", AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


}
