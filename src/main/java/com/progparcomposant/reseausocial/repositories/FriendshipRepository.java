package com.progparcomposant.reseausocial.repositories;

import com.progparcomposant.reseausocial.model.Friendship;
import com.progparcomposant.reseausocial.model.User;
import org.springframework.data.repository.CrudRepository;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    Iterable<Friendship> findFriendshipsByFirstUserId(Long userId);
}