package com.progparcomposant.reseausocial.repositories;

import com.progparcomposant.reseausocial.model.Invitation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {

    Iterable<Invitation> findAllByFirstUserId(Long userId);
    Optional<Invitation> findByFirstUserIdAndSecondUserId(Long firstUserId, Long secondUserId);
    void deleteByFirstUserIdAndSecondUserId(Long firstUserId, Long secondUserId);
    void deleteAllByFirstUserId(Long firstUserId);
}
