package com.progparcomposant.reseausocial.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(FriendshipId.class)
public class Friendship {

    @Id
    private Long firstUserId;
    @Id
    private Long secondUserId;

    private Timestamp friendshipDate;

    public Long getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(Long firstUserId) {
        this.firstUserId = firstUserId;
    }

    public Long getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(Long secondUserId) {
        this.secondUserId = secondUserId;
    }

    public Timestamp getFriendshipDate() {
        return friendshipDate;
    }

    public void setFriendshipDate(Timestamp friendshipDate) {
        this.friendshipDate = friendshipDate;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "firstUserId=" + firstUserId +
                ", secondUserId=" + secondUserId +
                ", friendshipDate=" + friendshipDate +
                '}';
    }
}
