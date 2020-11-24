package com.progparcomposant.reseausocial.model;

import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InvitationId implements Serializable {
    @Id
    private String firstUserId;
    @Id
    private String secondUserId;
}
