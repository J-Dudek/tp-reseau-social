package com.progparcomposant.reseausocial.controllers;


import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.model.Friendship;
import com.progparcomposant.reseausocial.model.Invitation;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.FriendshipRepository;
import com.progparcomposant.reseausocial.repositories.InvitationRepository;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final InvitationRepository invitationRepository;
    private final UserConverter userConverter;

    public UserController(UserConverter userConverter,UserRepository userRepository, FriendshipRepository friendshipRepository, InvitationRepository invitationRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.invitationRepository = invitationRepository;
        this.userConverter = userConverter;
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

    @PostMapping(path = "/")
    public UserDTO createUser( @RequestBody UserDTO newUserDto ){
        return userConverter.entityToDto(userRepository.save(this.userConverter.dtoToEntity(newUserDto)));
    }

    @PutMapping(path="/{userId}")
    public UserDTO updateUser( @PathVariable("userId") Long userId, @RequestBody UserDTO newUserDto){
        Optional<User> user = this.userRepository.findById(userId);
        if(!user.isPresent()){
            throw new NoSuchElementException("PostId inexistant");
        }else if( userId != newUserDto.getIdUser()){
            throw new IllegalArgumentException(String.valueOf(newUserDto.getIdUser()));
        }else{
            return userConverter.entityToDto(userRepository.save(this.userConverter.dtoToEntity(newUserDto)));
        }
    }

    @DeleteMapping(path = "/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId){ this.userRepository.deleteById(userId);}
}
