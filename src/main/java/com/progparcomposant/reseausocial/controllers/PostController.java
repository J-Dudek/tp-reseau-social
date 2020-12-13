package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.dto.PostDTO;
import com.progparcomposant.reseausocial.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/posts")
public class PostController {

    private final PostService postService;

    @GetMapping(path = "/all")
    public List<PostDTO> findPosts() {
        return this.postService.findPosts();
    }

    @GetMapping(path = "/{readerId}/read/{postId}")
    public PostDTO findPostByPostId(@PathVariable("readerId") Long readerId, @PathVariable("postId") Long postId) {
        return this.postService.findPostByPostId(readerId, postId);
    }

    @GetMapping(path = "/{readerId}/read/{authorId}/all")
    public List<PostDTO> findPostsByAuthorId(@PathVariable("readerId") Long readerId, @PathVariable("authorId") Long authorId) {
        return this.postService.findPostsByAuthorId(readerId, authorId);
    }

    @GetMapping(path = "/{readerId}/list/{postIds}")
    public List<PostDTO> findPostsByPostIds(@PathVariable("readerId") Long readerId, @PathVariable("postIds") List<Long> postIds) {
        return this.postService.findPostsByIds(readerId, postIds);
    }

    @PostMapping(path = "/create")
    public PostDTO createPost(@RequestBody PostDTO newPostDto) {
        return this.postService.createNewPost(newPostDto);
    }

    @PutMapping(path = "/{postId}/update")
    public PostDTO updatePost(@PathVariable("postId") Long postId, @RequestBody PostDTO newPostDto) {
        return this.postService.updatePost(postId, newPostDto);
    }

    @DeleteMapping(path = "/{postId}/delete")
    public void deletePostById(@PathVariable("postId") Long postId) {
        this.postService.deletePostById(postId);
    }

    @DeleteMapping(path = "/delete/all")
    public void deleteAllPosts() {
        this.postService.deleteAllPosts();
    }

    @DeleteMapping(path = "/{postIds}/delete/list")
    public void deletePostsByIds(@PathVariable("postIds") List<Long> postIds) {
        this.postService.deletePostsByIds(postIds);
    }
}
