package com.progparcomposant.reseausocial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(FriendshipId.class)
public class Friendship {

    @Id
    private String firstUserId;
    @Id
    private String secondUserId;
    private boolean hasFirstUserAccepted;
    private boolean hasSecondUserAccepted;
    private Date friendshipDate;
}
