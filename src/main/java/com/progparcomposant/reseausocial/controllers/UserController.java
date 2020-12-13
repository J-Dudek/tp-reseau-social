package com.progparcomposant.reseausocial.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.services.UserService;
import com.progparcomposant.reseausocial.views.UserViews;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/all")
    @JsonView(UserViews.Public.class)
    public List<UserDTO> findAllUsers() {
        return this.userService.findAllUsers();
    }

    @GetMapping(path = "/{userId}")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByUserId(@PathVariable("userId") Long userId) {
        return this.userService.findUserByUserId(userId);
    }

    @GetMapping(path = "/{userName}/name")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByName(@PathVariable("userName") String userName) {
        return this.userService.findUserByName(userName);
    }

    @GetMapping(path = "/{email}/email")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByEmail(@PathVariable("email") String email) {
        return this.userService.findUserByEmail(email);
    }

    @GetMapping(path = "/{phoneNumber}/phone")
    @JsonView(UserViews.Public.class)
    public UserDTO findUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        return this.userService.findUserByPhoneNumber(phoneNumber);
    }

    @PostMapping(path = "/create")
    public UserDTO createUser(@RequestBody UserDTO newUserDto) {
        return this.userService.createUser(newUserDto);
    }

    @PutMapping(path = "/update")
    public UserDTO updateUser(@RequestBody UserDTO newUserDto) {
        return this.userService.updateUser(newUserDto);
    }

    @DeleteMapping(path = "/{userId}/delete")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        this.userService.deleteUserById(userId);
    }

    @DeleteMapping("/delete/all")
    public void deleteAllUsers() {
        this.userService.deleteAllUsers();
    }

    @DeleteMapping(path = "/{userIds}/delete/list")
    public void deleteUsersByIds(@PathVariable("userIds") List<Long> userIds) {
        this.userService.deleteUsersByIds(userIds);
    }
}
