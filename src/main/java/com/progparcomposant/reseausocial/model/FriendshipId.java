package com.progparcomposant.reseausocial.model;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FriendshipId implements Serializable {

    private Long firstUserId;
    private Long secondUserId;
}
