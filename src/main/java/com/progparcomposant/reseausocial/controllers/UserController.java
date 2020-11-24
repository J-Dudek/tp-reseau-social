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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        //si l'id n'est pas passé par la session, aprés si on ne le passe pas par la session tt le monde peut voir les amis de tt le monde//
        if(!userRepository.existsById(userId)){
            throw new NoSuchElementException("Id ixesistant");
        }
        Iterable<Friendship> friendships = this.friendshipRepository.findFriendshipsByFirstUserIdOrSecondUserId(userId,userId);
        return StreamSupport.stream(friendships.spliterator(), false)
                .flatMap(e -> Stream.of(userRepository.findById(e.getFirstUserId()), userRepository.findById(e.getSecondUserId())))
                .filter(user->!user.get().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{userId}/invitations")
    public List<Invitation> findUserInvitations(@PathVariable("userId") Long userId) {
        Iterable<Invitation> invitations = this.invitationRepository.findAllByFirstUserId(userId);
        return StreamSupport.stream(invitations.spliterator(), false).collect(Collectors.toList());
    }
}
