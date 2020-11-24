package com.progparcomposant.reseausocial.model;

import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InvitationId implements Serializable {
    @Id
    private Long firstUserId;
    @Id
    private Long secondUserId;
}
