package com.progparcomposant.reseausocial.repositories;

import com.progparcomposant.reseausocial.model.Post;
import com.progparcomposant.reseausocial.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, Long> {
    Iterable<Post> findAllByUserId(Long userId);
    Iterable<Post> findAllByIdIn(List<Long> listIds);
    void deleteByIdIn(List<Long> listIds);
}
