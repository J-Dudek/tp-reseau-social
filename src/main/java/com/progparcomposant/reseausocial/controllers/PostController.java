package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.controllers.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.converters.PostConverter;
import com.progparcomposant.reseausocial.dto.PostDTO;
import com.progparcomposant.reseausocial.model.Post;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.PostRepository;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import org.apache.commons.collections4.IterableUtils;
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
    public List<PostDTO> findPosts() {
        Iterable<Post> posts = this.postRepository.findAll();
        if (IterableUtils.size(posts) > 0) {
            return this.postConverter.entityToDto(IterableUtils.toList(posts));
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.POST_NO_POSTS_IN_DATABASE.getErrorMessage());
        }
    }

    @GetMapping(path = "/{postId}")
    public PostDTO findPostByPostId(@PathVariable("postId") Long postId) {
        Optional<Post> post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            return this.postConverter.entityToDto(post.get());
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    @GetMapping(path = "/user/{userId}")
    public List<PostDTO> findPostsByUserId(@PathVariable("userId") Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            Iterable<Post> posts = this.postRepository.findAllByUserId(userId);
            if (IterableUtils.size(posts) > 0) {
                return this.postConverter.entityToDto(IterableUtils.toList(posts));
            } else {
                throw new NoSuchElementException(ErrorMessagesEnum.POST_NO_POST_YET.getErrorMessage());
            }
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    @GetMapping(path = "/list/{listIds}")
    public List<PostDTO> findListPostsByIds(@PathVariable("listIds") List<Long> ids) {
        Iterable<Post> posts = this.postRepository.findAllByIdIn((ids));
        if (IterableUtils.size(posts) > 0) {
            return this.postConverter.entityToDto(IterableUtils.toList(posts));
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    @PostMapping(path = "/")
    public PostDTO createPost(@RequestBody PostDTO newPostDto) {
        return postConverter.entityToDto(postRepository.save(this.postConverter.dtoToEntity(newPostDto)));
    }

    @PutMapping(path = "/{postId}")
    public PostDTO updatePost(@PathVariable("postId") Long postId, @RequestBody PostDTO newPostDto) {
        Optional<Post> post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            return postConverter.entityToDto(postRepository.save(this.postConverter.dtoToEntity(newPostDto)));
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    @DeleteMapping(path = "/{postId}")
    public void deletePostById(@PathVariable("postId") Long postId) {
        Optional<Post> post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            this.postRepository.deleteById(postId);
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    @DeleteMapping
    public void deleteAllPost() {
        this.postRepository.deleteAll();
    }

    @DeleteMapping(path = "/list/{listIds}")
    public void deletePostsByIds(@PathVariable("listIds") List<Long> listIds) {
        for (Long ids : listIds) {
            this.postRepository.deleteById(ids);
        }
    }
}
