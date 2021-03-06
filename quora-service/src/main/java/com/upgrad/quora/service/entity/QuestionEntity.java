package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

@Entity

@Table(name = "QUESTION",schema = "public")
@NamedQueries({
        @NamedQuery(name = "getAllQuestion", query = "select u from QuestionEntity u"),
        @NamedQuery(name = "getQuestionById",query = "select u from QuestionEntity u where u.uuid = :uuid"),
        @NamedQuery(name = "getQuestionByUser",query = "select u from QuestionEntity u where u.user = :userId")
})
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "UUID",nullable = false)
    @Size(max = 200)
    private String uuid;

    @Column(name = "CONTENT",nullable = false)
    @Size(max = 500)
    private String content;

    @Column(name = "DATE",nullable = false)
    private ZonedDateTime date;

    public List<AnswerEntity> getAnswerEntityList() {
        return answerEntityList;
    }

    public void setAnswerEntityList(List<AnswerEntity> answerEntityList) {
        this.answerEntityList = answerEntityList;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "questionEntity",cascade = CascadeType.REMOVE)
    private List<AnswerEntity> answerEntityList;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
