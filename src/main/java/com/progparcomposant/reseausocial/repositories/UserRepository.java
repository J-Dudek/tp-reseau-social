package com.progparcomposant.reseausocial.repositories;

import com.progparcomposant.reseausocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUsersByUsername(String userName);
    List<User> findByIdIn(List<Long> ids);
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByPhoneNumber(String phoneNumber);
}
