package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.converters.PostConverter;
import com.progparcomposant.reseausocial.model.Post;
import com.progparcomposant.reseausocial.repositories.PostRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    private final PostRepository postRepository;
    private final PostConverter postConverter;

    public PostController(PostRepository postRepository, PostConverter postConverter) {
        this.postRepository = postRepository;
        this.postConverter = postConverter;
    }

    @GetMapping
    public Iterable<Post> findPosts(){ return this.postRepository.findAll(); }

    @GetMapping(path="/{postId}")
    public Optional<Post> findPost(@PathVariable("postId") Long postId){ return this.postRepository.findById(postId);}


}
