package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.exceptions.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.converters.PostConverter;
import com.progparcomposant.reseausocial.dto.PostDTO;
import com.progparcomposant.reseausocial.model.Post;
import com.progparcomposant.reseausocial.repositories.PostRepository;
import com.progparcomposant.reseausocial.services.FriendshipService;
import com.progparcomposant.reseausocial.services.PostService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/posts")
public class PostController {

    private final PostRepository postRepository;
    private final PostConverter postConverter;
    private final FriendshipService friendshipService;
    private final PostService postService;

    @GetMapping(path = "/all")
    public List<PostDTO> findPosts() {
        Iterable<Post> posts = this.postRepository.findAll();
        if (IterableUtils.size(posts) > 0) {
            return this.postConverter.entityToDto(IterableUtils.toList(posts));
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.POST_NO_POSTS_IN_DATABASE.getErrorMessage());
        }
    }

    @GetMapping(path = "/{readerId}/{postId}")
    public PostDTO findPostByPostId(@PathVariable("readerId") Long readerId, @PathVariable("postId") Long postId) {
        PostDTO postDTO = this.postService.findPostByPostId(postId);
        if (this.friendshipService.isFriendshipExists(readerId, postDTO.getUserId()) || postDTO.isPublic() || postDTO.getUserId().equals(readerId)) {
            return postDTO;
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.POST_NOT_PUBLIC.getErrorMessage());
        }
    }

    @GetMapping(path = "/{readerId}/{authorId}")
    public List<PostDTO> findPostsByAuthorId(@PathVariable("readerId") Long readerId, @PathVariable("authorId") Long authorId) {
        if (this.friendshipService.isFriendshipExists(readerId, authorId)) {
            return this.postService.findAllPostsByUserId(authorId);
        } else {
            return this.postService.findPublicPostsByUserId(authorId);
        }
    }

    @GetMapping(path = "/{readerId}/list/{postIds}")
    public List<PostDTO> findListPostsByPostIds(@PathVariable("readerId") Long readerId, @PathVariable("postIds") List<Long> postIds) {
        List<PostDTO> postsFromDatabase = this.postService.findPostsByIds(postIds);
        List<PostDTO> postsToReturn = new ArrayList<>();
        for (PostDTO postDTO : postsFromDatabase) {
            if (this.friendshipService.isFriendshipExists(readerId, postDTO.getUserId()) || postDTO.isPublic()) {
                postsToReturn.add(postDTO);
            }
        }
        return postsToReturn;
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
