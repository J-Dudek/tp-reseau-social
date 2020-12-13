package com.progparcomposant.reseausocial.services;

import com.progparcomposant.reseausocial.converters.PostConverter;
import com.progparcomposant.reseausocial.dto.PostDTO;
import com.progparcomposant.reseausocial.exceptions.PostException;
import com.progparcomposant.reseausocial.exceptions.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.model.Post;
import com.progparcomposant.reseausocial.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostConverter postConverter;
    private final FriendshipService friendshipService;

    public List<PostDTO> findPosts() {
        Iterable<Post> posts = this.postRepository.findAll();
        if (IterableUtils.size(posts) > 0) {
            return this.postConverter.entityToDto(IterableUtils.toList(posts));
        } else {
            throw new PostException(ErrorMessagesEnum.POST_NO_POSTS_IN_DATABASE.getErrorMessage());
        }
    }

    public PostDTO findPostByPostId(Long readerId, Long postId) {
        Optional<Post> post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            PostDTO postDTO = this.postConverter.entityToDto(post.get());
            if (this.friendshipService.isFriendshipExists(readerId, postDTO.getUserId()) || postDTO.isPublic() || postDTO.getUserId().equals(readerId)) {
                return postDTO;
            } else {
                throw new PostException(ErrorMessagesEnum.POST_NOT_PUBLIC.getErrorMessage());
            }
        } else {
            throw new PostException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    public List<PostDTO> findPostsByAuthorId(Long readerId, Long authorId) {
        if (this.friendshipService.isFriendshipExists(readerId, authorId)) {
            return this.findAllPostsByUserId(authorId);
        } else {
            return this.findPublicPostsByUserId(authorId);
        }
    }

    public List<PostDTO> findPostsByIds(Long readerId, List<Long> postIds) {
        Iterable<Post> posts = this.postRepository.findAllByIdIn((postIds));
        if (IterableUtils.size(posts) > 0) {
            List<PostDTO> postsFromDatabase = this.postConverter.entityToDto(IterableUtils.toList(posts));
            List<PostDTO> postsToReturn = new ArrayList<>();
            for (PostDTO postDTO : postsFromDatabase) {
                if (this.friendshipService.isFriendshipExists(readerId, postDTO.getUserId()) || postDTO.isPublic()) {
                    postsToReturn.add(postDTO);
                }
            }
            return postsToReturn;
        } else {
            throw new PostException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
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
            throw new PostException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    public void deletePostById(Long postId) {
        Optional<Post> post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            this.postRepository.deleteById(postId);
        } else {
            throw new PostException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }

    public void deleteAllPosts() {
        Iterable<Post> posts = this.postRepository.findAll();
        if (IterableUtils.size(posts) > 0) {
            this.postRepository.deleteAll();
        } else {
            throw new PostException(ErrorMessagesEnum.POST_NO_POSTS_IN_DATABASE.getErrorMessage());
        }
    }

    public void deletePostsByIds(List<Long> postIds) {
        Iterable<Post> posts = this.postRepository.findAllByIdIn(postIds);
        if (IterableUtils.size(posts) > 0) {
            List<PostDTO> postDTOS = this.postConverter.entityToDto(IterableUtils.toList(posts));
            postDTOS.forEach(postDTO -> this.deletePostById(postDTO.getIdPost()));
        } else {
            throw new PostException(ErrorMessagesEnum.POST_NOT_FOUND.getErrorMessage());
        }
    }


    private List<PostDTO> findAllPostsByUserId(Long userId) {
        Iterable<Post> posts = this.postRepository.findAllByUserId(userId);
        if (IterableUtils.size(posts) > 0) {
            return this.postConverter.entityToDto(IterableUtils.toList(posts));
        } else {
            throw new PostException(ErrorMessagesEnum.POST_NO_POST_YET.getErrorMessage());
        }
    }

    private List<PostDTO> findPublicPostsByUserId(Long userId) {
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
            throw new PostException(ErrorMessagesEnum.POST_NO_POST_YET.getErrorMessage());
        }
    }
}
