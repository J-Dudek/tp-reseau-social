package com.progparcomposant.reseausocial.controllers;


import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.model.Friendship;
import com.progparcomposant.reseausocial.model.Invitation;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.FriendshipRepository;
import com.progparcomposant.reseausocial.repositories.InvitationRepository;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
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
    public List<UserDTO> findUsers() {
        Iterable<User> users = this.userRepository.findAll();
        if (IterableUtils.size(users) > 0) {
            return this.userConverter.entityToDto(IterableUtils.toList(users));
        } else {
            throw new NoSuchElementException("Aucun user dans la bdd");
        }
    }

    @GetMapping(path = "/{userId}")
    public UserDTO findUser(@PathVariable("userId") Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new NoSuchElementException("Ce user n'existe pas");
        }
    }

    @GetMapping(path = "/name/{userName}")
    public UserDTO findUserByName(@PathVariable("userName") String userName){
        Optional<User> user = this.userRepository.findUsersByUsername(userName);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new NoSuchElementException("Aucun user avec ce nom");
        }
    }

    @PostMapping(path = "/")
    public UserDTO createUser( @RequestBody UserDTO newUserDto ){
        return userConverter.entityToDto(userRepository.save(this.userConverter.dtoToEntity(newUserDto)));
    }

    @PutMapping(path="/{userId}")
    public UserDTO updateUser( @PathVariable("userId") Long userId, @RequestBody UserDTO newUserDto){
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isPresent()){
            return userConverter.entityToDto(userRepository.save(this.userConverter.dtoToEntity(newUserDto)));
        }else if(!userId.equals(newUserDto.getIdUser())){
            throw new IllegalArgumentException(String.valueOf(newUserDto.getIdUser()));
        }else{
            throw new NoSuchElementException("PostId inexistant");
        }
    }

    @DeleteMapping(path = "/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId){
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            this.userRepository.deleteById(userId);
        } else {
            throw new NoSuchElementException("Ce user n'existe pas");
        }
    }

    @DeleteMapping(path = "")
    public void deleteAllUsers(){ this.userRepository.deleteAll();}

    @DeleteMapping(path = "/list/{listIds}")
    public void deleteUsersByIds(@PathVariable("listIds") List<Long> listIds){
        for(Long id : listIds) {
            this.userRepository.deleteById(id);
        }
    }
}
