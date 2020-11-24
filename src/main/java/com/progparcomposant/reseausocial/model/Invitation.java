package com.progparcomposant.reseausocial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(InvitationId.class)
public class Invitation {

    @Id
    private String firstUserId;
    @Id
    private String secondUserId;
    private Timestamp invitationDate;
}
