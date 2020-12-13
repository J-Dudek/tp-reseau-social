package com.progparcomposant.reseausocial.controllers;


import com.fasterxml.jackson.annotation.JsonView;
import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.exceptions.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import com.progparcomposant.reseausocial.views.UserViews;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserController(UserConverter userConverter, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @GetMapping(path = "/all")
    @JsonView(UserViews.Public.class)
    public List<UserDTO> findAllUsers() {
        Iterable<User> users = this.userRepository.findAll();
        if (IterableUtils.size(users) > 0) {
            return this.userConverter.entityToDto(IterableUtils.toList(users));
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NO_USERS_IN_DATABASE.getErrorMessage());
        }
    }

    @GetMapping(path = "/{userId}")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByUserId(@PathVariable("userId") Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    @GetMapping(path = "/{userName}/name")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByName(@PathVariable("userName") String userName) {
        Optional<User> user = this.userRepository.findUsersByUsername(userName);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NO_USER_WITH_THAT_NAME.getErrorMessage());
        }
    }

    @GetMapping(path = "/{email}/email")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByEmail(@PathVariable("email") String email) {
        Optional<User> user = this.userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NO_USER_WITH_THAT_EMAIL.getErrorMessage());
        }
    }

    @GetMapping(path = "/{phoneNumber}/phone")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        Optional<User> user = this.userRepository.findUserByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NO_USER_WITH_THAT_PHONENUMBER.getErrorMessage());
        }
    }

    @PostMapping(path = "/create")
    public UserDTO createUser(@RequestBody UserDTO newUserDto) {
        return userConverter.entityToDto(userRepository.save(this.userConverter.dtoToEntity(newUserDto)));
    }

    @PutMapping(path="/{userId}/update")
    public UserDTO updateUser( @PathVariable("userId") Long userId, @RequestBody UserDTO newUserDto){
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isPresent()){
            return userConverter.entityToDto(userRepository.save(this.userConverter.dtoToEntity(newUserDto)));
        }else if(!userId.equals(newUserDto.getIdUser())){
            throw new SocialNetworkException(String.valueOf(newUserDto.getIdUser()));
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    @DeleteMapping(path = "/{userId}/delete")
    public void deleteUserById(@PathVariable("userId") Long userId){
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            this.userRepository.deleteById(userId);
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    @DeleteMapping("/delete/all")
    public void deleteAllUsers() {
        Iterable<User> users = this.userRepository.findAll();
        if (IterableUtils.size(users) > 0) {
            this.userRepository.deleteAll();
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NO_USERS_IN_DATABASE.getErrorMessage());
        }
    }

    @DeleteMapping(path = "/{ids}/delete/list")
    public void deleteUsersByIds(@PathVariable("ids") List<Long> ids) {
        Iterable<User> users = this.userRepository.findByIdIn(ids);
        if (IterableUtils.size(users) > 0) {
            for (Long id : ids) {
                this.userRepository.deleteById(id);
            }
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }
}
