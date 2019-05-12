package com.upgrad.quora.service.entity;


import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ANSWER",schema = "public")
@NamedQueries({
        @NamedQuery(name = "getAnswerByQuestion",query = "select u from AnswerEntity u where u.questionEntity =:question"),
        @NamedQuery(name = "getAnswerById", query = "select u from AnswerEntity u where u.uuid = :uuid")

})

@Service
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "UUID",nullable = false)
    @Size(max = 200)
    private String uuid;

    @Column(name = "ANS",nullable = false)
    @Size(max = 500)
    private String ans;

    @Column(name = "DATE",nullable = false)
    private ZonedDateTime date;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "question_id")
    private QuestionEntity questionEntity;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private UserEntity user;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String content) {
        this.ans = ans;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public QuestionEntity getQuestionEntity() {
        return questionEntity;
    }

    public void setQuestionEntity(QuestionEntity questionEntity) {
        this.questionEntity = questionEntity;
    }
}