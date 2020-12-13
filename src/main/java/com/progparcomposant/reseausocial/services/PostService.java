package com.progparcomposant.reseausocial.services;

import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.exceptions.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.converters.PostConverter;
import com.progparcomposant.reseausocial.dto.PostDTO;
import com.progparcomposant.reseausocial.model.Post;
import com.progparcomposant.reseausocial.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostConverter postConverter;

    public PostDTO findPostByPostId(Long postId) {
        Optional<Post> post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            return this.postConverter.entityToDto(post.get());
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    public List<PostDTO> findAllPostsByUserId(Long userId) {
        Iterable<Post> posts = this.postRepository.findAllByUserId(userId);
        if (IterableUtils.size(posts) > 0) {
            return this.postConverter.entityToDto(IterableUtils.toList(posts));
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.POST_NO_POST_YET.getErrorMessage());
        }
    }

    public List<PostDTO> findPublicPostsByUserId(Long userId) {
        Iterable<Post> posts = this.postRepository.findAllByUserId(userId);
        if (IterableUtils.size(posts) > 0) {
            List<PostDTO> postsToReturn = new ArrayList<>();
            posts.forEach(post -> {
                if (post.isPublic()) {
                    postsToReturn.add(this.postConverter.entityToDto(post));
                }
            });
            return postsToReturn;
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.POST_NO_POST_YET.getErrorMessage());
        }
    }

    public List<PostDTO> findPostsByIds(List<Long> postIds) {
        Iterable<Post> posts = this.postRepository.findAllByIdIn((postIds));
        if (IterableUtils.size(posts) > 0) {
            return this.postConverter.entityToDto(IterableUtils.toList(posts));
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    public PostDTO createNewPost(PostDTO postDTO) {
        return postConverter.entityToDto(postRepository.save(this.postConverter.dtoToEntity(postDTO)));
    }

    public PostDTO updatePost(Long postId, PostDTO newPostDTO) {
        Optional<Post> post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            return postConverter.entityToDto(postRepository.save(this.postConverter.dtoToEntity(newPostDTO)));
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    public void deletePostById(Long postId) {
        Optional<Post> post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            this.postRepository.deleteById(postId);
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    public void deleteAllPosts() {
        Iterable<Post> posts = this.postRepository.findAll();
        if (IterableUtils.size(posts) > 0) {
            this.postRepository.deleteAll();
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.POST_NO_POSTS_IN_DATABASE.getErrorMessage());
        }
    }

    public void deletePostsByIds(List<Long> postIds) {
        List<PostDTO> postDTOS = findPostsByIds(postIds);
        postDTOS.forEach(postDTO -> this.deletePostById(postDTO.getIdPost()));
    }
}
