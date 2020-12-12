package com.progparcomposant.reseausocial.repositories;

import com.progparcomposant.reseausocial.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    Iterable<Post> findAllByUserId(Long userId);
    Iterable<Post> findAllByIdIn(List<Long> listIds);
    void deleteByIdIn(List<Long> listIds);
}
