package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.model.Friendship;
import com.progparcomposant.reseausocial.model.Invitation;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.FriendshipRepository;
import com.progparcomposant.reseausocial.repositories.InvitationRepository;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final InvitationRepository invitationRepository;

    public UserController(UserRepository userRepository, FriendshipRepository friendshipRepository, InvitationRepository invitationRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.invitationRepository = invitationRepository;
    }

    @GetMapping
    public Iterable<User> findUsers() {
        return this.userRepository.findAll();
    }

    @GetMapping(path = "/{userId}")
    public Optional<User> findUser(@PathVariable("userId") Long userId) {
        return this.userRepository.findById(userId);
    }

    @GetMapping(path = "/{userId}/friends")
    public List<Optional<User>> findUserFriends(@PathVariable("userId") Long userId) {
        Iterable<Friendship> friendships = this.friendshipRepository.findFriendshipsByFirstUserIdOrSecondUserId(userId,userId);
        List<Optional<User>> friends = new ArrayList<>();
        for (Friendship friendship : friendships) {
            if (friendship.getFirstUserId().equals(userId)) {
                friends.add(this.userRepository.findById(friendship.getSecondUserId()));
            }else if(friendship.getSecondUserId().equals(userId)){
                friends.add(this.userRepository.findById(friendship.getFirstUserId()));
            }

        }
        return friends;
    }

    @GetMapping(path = "/{userId}/invitations")
    public List<Invitation> findUserInvitations(@PathVariable("userId") Long userId) {
        Iterable<Invitation> invitations = this.invitationRepository.findAllByFirstUserId(userId);
        return StreamSupport.stream(invitations.spliterator(), false).collect(Collectors.toList());
    }
}
