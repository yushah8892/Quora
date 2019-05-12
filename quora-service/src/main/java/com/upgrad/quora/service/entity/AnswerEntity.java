package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

//@Entity annotation is used to create a JPA entity. Entity class is an annotated POJO (Plain Old Java Object.
@Entity
//Using @Table annotation we can create a table in our db.Here we are creating a table named answer in the db.
@Table(name="answer", schema = "public")
@NamedQueries({
        @NamedQuery(name = "getAnswerById", query = "select u from AnswerEntity u where u.uuid = :uuid"),
        @NamedQuery(name = "getAnswerByQuestionId", query = "select u from AnswerEntity u where u.questionEntity = :questionId")
})
public class AnswerEntity {

    //@Id annotation is used to create primary key in the table.Here id will be automatically changed since we are using
    //@GeneratedValue annotation.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    //The uuid will be exposed since exposing the actual ID may lead to compromising our DB.
    @Column(name="uuid")
    @Size(max = 200)
    private String uuid;

    @Column(name="ans")
    @Size(max = 255)
    private String answer;

    @Column(name="date")
    private ZonedDateTime date;

    //We are using Many to One relationship since a user can answer many questions.
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    //Many to One relationship is used here since a question can have multiple answers.
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "question_id")
    private QuestionEntity questionEntity;

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public QuestionEntity getQuestionEntity() {
        return questionEntity;
    }

    public void setQuestionEntity(QuestionEntity questionEntity) {
        this.questionEntity = questionEntity;
    }
}
