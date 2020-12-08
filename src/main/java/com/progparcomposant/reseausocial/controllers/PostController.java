package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.converters.PostConverter;
import com.progparcomposant.reseausocial.dto.PostDTO;
import com.progparcomposant.reseausocial.model.Post;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.PostRepository;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Iterable<PostDTO> findPosts(){ return this.postConverter.entityToDto((List<Post>) this.postRepository.findAll()); }

    @GetMapping(path="/{postId}")
    public PostDTO findPost(@PathVariable("postId") Long postId) throws NoSuchElementException{

        Optional<Post> post = this.postRepository.findById(postId);
        if(post.isPresent()){
            return this.postConverter.entityToDto(post.get());
        }else{
            throw new NoSuchElementException("UserId inexistant");
        }
    }

    @GetMapping(path = "/user/{userId}")
    public Iterable<PostDTO> findPostsByUser(@PathVariable("userId") Long userId){
/*        if(!userRepository.existsById(userId)){
            throw new NoSuchElementException("UserId inexistant")
        }*/
        return this.postConverter.entityToDto((List<Post>) this.postRepository.findAllByUserId(userId));
    }

    @GetMapping(path="/list/{listIds}")
    public Iterable<PostDTO> findListPostsByIds(@PathVariable("listIds") List<Long> listIds){
        return this.postConverter.entityToDto((List<Post>) this.postRepository.findAllByIdIn(listIds));}

    @PostMapping(path="/")
    public PostDTO createPost( @RequestBody PostDTO newPostDto){
        return  postConverter.entityToDto(postRepository.save(this.postConverter.dtoToEntity(newPostDto)));
    }
}
