package com.progparcomposant.reseausocial.repositories;

import com.progparcomposant.reseausocial.model.Invitation;
import org.springframework.data.repository.CrudRepository;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {

    Iterable<Invitation> findAllByFirstUserId(Long userId);
}
