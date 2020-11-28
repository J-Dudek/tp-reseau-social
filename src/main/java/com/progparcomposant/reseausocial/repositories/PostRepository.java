package com.progparcomposant.reseausocial.repositories;

import com.progparcomposant.reseausocial.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}
