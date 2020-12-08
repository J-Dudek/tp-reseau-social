package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.converters.PostConverter;
import com.progparcomposant.reseausocial.model.Post;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.PostRepository;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostConverter postConverter;

    public PostController(PostRepository postRepository, UserRepository userRepository, PostConverter postConverter) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postConverter = postConverter;
    }

    @GetMapping
    public Iterable<Post> findPosts(){ return this.postRepository.findAll(); }

    @GetMapping(path="/{postId}")
    public Optional<Post> findPost(@PathVariable("postId") Long postId){ return this.postRepository.findById(postId);}

    @GetMapping(path = "/user/{userId}")
    public Iterable<Post> findPostsByUser(@PathVariable("userId") Long userId){
/*        if(!userRepository.existsById(userId)){
            throw new NoSuchElementException("UserId inexistant")
        }*/
        return this.postRepository.findAllByUserId(userId);
    }

    
}
