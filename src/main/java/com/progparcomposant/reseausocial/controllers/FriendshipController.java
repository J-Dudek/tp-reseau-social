package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.controllers.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.converters.FriendshipConverter;
import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.FriendshipDTO;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.model.Friendship;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.FriendshipRepository;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/friends")
public class FriendshipController {

    private final FriendshipRepository friendshipRepository;
    private final FriendshipConverter friendshipConverter;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public FriendshipController(FriendshipRepository friendshipRepository, FriendshipConverter friendshipConverter, UserRepository userRepository, UserConverter userConverter) {
        this.friendshipRepository = friendshipRepository;
        this.friendshipConverter = friendshipConverter;
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @GetMapping("/all")
    public List<FriendshipDTO> findAllFriendships() {
        Iterable<Friendship> friendships = this.friendshipRepository.findAll();
        if (IterableUtils.size(friendships) > 0) {
            return this.friendshipConverter.entityToDto(IterableUtils.toList(friendships));
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.FRIENDSHIP_NO_FRIENDSHIPS_IN_DATABASE.getErrorMessage());
        }
    }

    @GetMapping(path = "/{userId}")
    public List<UserDTO> findFriendsByUserId(@PathVariable("userId") Long userId) {
        Iterable<Friendship> friendships = this.friendshipRepository.findFriendshipsByFirstUserIdOrSecondUserId(userId, userId);
        if (IterableUtils.size(friendships) > 0) {
            List<Long> friendsIds = new ArrayList<>();
            friendships.forEach(friendship -> friendsIds.add(friendship.getSecondUserId()));
            return this.userConverter.entityToDto(this.userRepository.findByIdIn(friendsIds));
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    @GetMapping(path = "/{firstUserId}/{secondUserId}")
    public UserDTO findFriendByFriendId(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        Optional<Friendship> friendship = this.friendshipRepository.findByFirstUserIdAndSecondUserId(firstUserId, secondUserId);
        if (friendship.isPresent()) {
            FriendshipDTO friendshipDTO = this.friendshipConverter.entityToDto(friendship.get());
            Long friendId = friendshipDTO.getFirstUserId().equals(secondUserId) ? friendshipDTO.getSecondUserId() : friendshipDTO.getFirstUserId();
            Optional<User> user = this.userRepository.findById(friendId);
            if (user.isPresent()) {
                return this.userConverter.entityToDto(user.get());
            } else {
                throw new NoSuchElementException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
            }
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.FRIENDSHIP_NOT_FOUND.getErrorMessage());
        }
    }

    @DeleteMapping(path = "/{firstUserId}/delete/{secondUserId}")
    public void deleteFriendByFriendId(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        Optional<Friendship> friendship = this.friendshipRepository.findByFirstUserIdAndSecondUserId(firstUserId, secondUserId);
        if (friendship.isEmpty()) {
            friendship = this.friendshipRepository.findByFirstUserIdAndSecondUserId(secondUserId, firstUserId);
            if (friendship.isEmpty()) {
                throw new NoSuchElementException(ErrorMessagesEnum.FRIENDSHIP_NOT_FOUND.getErrorMessage());
            }
        }
        FriendshipDTO friendshipDTO = this.friendshipConverter.entityToDto(friendship.get());
        this.friendshipRepository.deleteByFirstUserIdAndSecondUserId(friendshipDTO.getFirstUserId(), friendshipDTO.getSecondUserId());
    }

    @DeleteMapping(path = "/{userId}/delete/all")
    public void deleteAllUserFriends(@PathVariable("userId") Long userId) {
        Iterable<Friendship> friendships = this.friendshipRepository.findFriendshipsByFirstUserIdOrSecondUserId(userId, userId);
        if (IterableUtils.size(friendships) > 0) {
            this.friendshipRepository.deleteFriendshipsByFirstUserIdOrSecondUserId(userId, userId);
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.FRIENDSHIP_USER_WITH_NO_FRIENDS.getErrorMessage());
        }
    }
}
