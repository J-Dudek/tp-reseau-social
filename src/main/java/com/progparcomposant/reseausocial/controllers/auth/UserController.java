package com.progparcomposant.reseausocial.controllers.auth;

import com.fasterxml.jackson.annotation.JsonView;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.services.UserService;
import com.progparcomposant.reseausocial.views.UserViews;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/all")
    @JsonView(UserViews.Public.class)
    public List<UserDTO> findAllUsers() {
        try {
            return this.userService.findAllUsers();
        } catch (
                SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{userId}")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByUserId(@PathVariable("userId") Long userId) {
        try {
            return this.userService.findUserByUserId(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{userName}/name")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByName(@PathVariable("userName") String userName) {
        try {
            return this.userService.findUserByName(userName);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{email}/email")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByEmail(@PathVariable("email") String email) {
        try {
            return this.userService.findUserByEmail(email);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{phoneNumber}/phone")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        try {
            return this.userService.findUserByPhoneNumber(phoneNumber);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    public UserDTO createUser(@RequestBody UserDTO newUserDto) {
        try {
            return this.userService.createUser(newUserDto);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/update")
    public UserDTO updateUser(@RequestBody UserDTO newUserDto) {
        try {
            return this.userService.updateUser(newUserDto);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{userId}/delete")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        try {
            this.userService.deleteUserById(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/all")
    public void deleteAllUsers() {
        try {
            this.userService.deleteAllUsers();
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{userIds}/delete/list")
    public void deleteUsersByIds(@PathVariable("userIds") List<Long> userIds) {
        try {
            this.userService.deleteUsersByIds(userIds);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
