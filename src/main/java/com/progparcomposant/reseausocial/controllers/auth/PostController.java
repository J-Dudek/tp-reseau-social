package com.progparcomposant.reseausocial.controllers.auth;

import com.progparcomposant.reseausocial.configuration.SwaggerConfig;
import com.progparcomposant.reseausocial.dto.PostDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.services.PostService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/posts")
@Api(tags = { SwaggerConfig.POST })
public class PostController {

    private final PostService postService;

    @GetMapping(path = "/all")
    public List<PostDTO> findPosts() {
        try {
            return this.postService.findPosts();
        } catch (
                SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{readerId}/read/{postId}")
    public PostDTO findPostByPostId(@PathVariable("readerId") Long readerId, @PathVariable("postId") Long postId) {
        try {
            return this.postService.findPostByPostId(readerId, postId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{readerId}/read/{authorId}/all")
    public List<PostDTO> findPostsByAuthorId(@PathVariable("readerId") Long readerId, @PathVariable("authorId") Long authorId) {
        try {
            return this.postService.findPostsByAuthorId(readerId, authorId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{readerId}/list/{postIds}")
    public List<PostDTO> findPostsByPostIds(@PathVariable("readerId") Long readerId, @PathVariable("postIds") List<Long> postIds) {
        try {
            return this.postService.findPostsByIds(readerId, postIds);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    public PostDTO createPost(@RequestBody PostDTO newPostDto) {
        try {
            return this.postService.createNewPost(newPostDto);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{postId}/update")
    public PostDTO updatePost(@PathVariable("postId") Long postId, @RequestBody PostDTO newPostDto) {
        try {
            return this.postService.updatePost(postId, newPostDto);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{postId}/delete")
    public void deletePostById(@PathVariable("postId") Long postId) {
        try {
            this.postService.deletePostById(postId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/delete/all")
    public void deleteAllPosts() {
        try {
            this.postService.deleteAllPosts();
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{postIds}/delete/list")
    public void deletePostsByIds(@PathVariable("postIds") List<Long> postIds) {
        try {
            this.postService.deletePostsByIds(postIds);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
