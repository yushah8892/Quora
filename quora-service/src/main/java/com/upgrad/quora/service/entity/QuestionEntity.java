package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "QUESTION",schema = "public")
@NamedQueries({
        @NamedQuery(name = "getAllQuestion", query = "select u from QuestionEntity u"),
        @NamedQuery(name = "getQuestionById",query = "select u from QuestionEntity u where u.uuid = :uuid")
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

    @ManyToOne(cascade = CascadeType.REMOVE)
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
