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

    /**
     *
     * @return Iterable<PostDTO>
     */
    @GetMapping
    public Iterable<PostDTO> findPosts(){ return this.postConverter.entityToDto((List<Post>) this.postRepository.findAll()); }

    /**
     *
     * @param postId
     * @return PostDTO
     * @throws NoSuchElementException
     */
    @GetMapping(path="/{postId}")
    public PostDTO findPostById(@PathVariable("postId") Long postId) throws NoSuchElementException{

        Optional<Post> post = this.postRepository.findById(postId);
        //Check if @param postId is in Database
        if(post.isPresent()){
            return this.postConverter.entityToDto(post.get());
        }else{
            throw new NoSuchElementException("UserId inexistant");
        }
    }

    /**
     *
     * @param userId
     * @return
     */
    @GetMapping(path = "/user/{userId}")
    public Iterable<PostDTO> findPostsByUser(@PathVariable("userId") Long userId){
        if(!userRepository.existsById(userId)){
            throw new NoSuchElementException("UserId inexistant");
        }
        return this.postConverter.entityToDto((List<Post>) this.postRepository.findAllByUserId(userId));
    }

    /**
     *
     * @param listIds
     * @return Iterable<PostDTO>
     */
    @GetMapping(path="/list/{listIds}")
    public Iterable<PostDTO> findListPostsByIds(@PathVariable("listIds") List<Long> listIds){
        return this.postConverter.entityToDto((List<Post>) this.postRepository.findAllByIdIn(listIds));}

    /**
     *
      * @param newPostDto
     * @return PostDTO
     */
    @PostMapping(path="/")
    public PostDTO createPost( @RequestBody PostDTO newPostDto){
        return  postConverter.entityToDto(postRepository.save(this.postConverter.dtoToEntity(newPostDto)));
    }

    /**
     *
     * @param postId
     * @param newPostDto
     * @return PostDTO
     */
    @PutMapping(path="/{postId}")
    public PostDTO updatePost( @PathVariable("postId") Long postId, @RequestBody PostDTO newPostDto){
        Optional<Post> post = this.postRepository.findById(postId);
        if(!post.isPresent()){
            throw new NoSuchElementException("PostId inexistant");
        }else if( post.get().getId() != newPostDto.getIdPost()){
            throw new IllegalArgumentException();
        }
        else{
            return  postConverter.entityToDto(postRepository.save(this.postConverter.dtoToEntity(newPostDto)));
        }
    }

    /**
     *
     * @param postId
     */
    @DeleteMapping(path = "/{postId}")
    public void deletePostById(@PathVariable ("postId") Long postId){
        this.postRepository.deleteById(postId);
    }

    /**
     *
     */
    @DeleteMapping(path = "")
    public void deleteAllPost(){ this.postRepository.deleteAll();}

    /**
     * Should be optimized using deleteByIdIn from repository
     * @param listIds
     */
    @DeleteMapping(path = "/list/{listIds}")
    public void deletePostsByIds(@PathVariable("listIds") List<Long> listIds){
        for (Long ids : listIds ) {
            this.postRepository.deleteById(ids);
        }
    }
}
