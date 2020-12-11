package com.progparcomposant.reseausocial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post extends BaseEntity {

    private String message;
    private Timestamp publicationDate;
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
