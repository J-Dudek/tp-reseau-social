package com.progparcomposant.reseausocial.repositories;

import com.progparcomposant.reseausocial.model.Friendship;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    Iterable<Friendship> findFriendshipsByFirstUserIdOrSecondUserId(Long firstUserId, Long secondUserId);
    Optional<Friendship> findByFirstUserIdAndSecondUserId(Long firstUserId, Long secondUserId);
    void deleteFriendshipByFirstUserIdAndSecondUserId(Long firstUserId, Long secondUserId);
    void deleteFriendshipsByFirstUserIdOrSecondUserId(Long firstUserId, Long secondUserId);
}