package com.progparcomposant.reseausocial.repositories;

import com.progparcomposant.reseausocial.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
