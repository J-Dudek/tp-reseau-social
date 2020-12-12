package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.controllers.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping(path = "/account")
public class MainController {

    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MainController(UserConverter userConverter, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public void register(@RequestBody UserDTO userDTO) {
        if (userRepository.findUserByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException(ErrorMessagesEnum.AUTH_EMAIL_ALREADY_ASSIGNED.getErrorMessage());
        } else {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userDTO.setSignInDate(new Timestamp(System.currentTimeMillis()));
            this.userRepository.save(this.userConverter.dtoToEntity(userDTO));
        }
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody UserDTO userDTO) {
        Optional<User> user = userRepository.findUserByUsername(userDTO.getUsername());
        if (user.isPresent() && passwordEncoder.matches(userDTO.getPassword(), user.get().getPassword())) {
            return userConverter.entityToDto(user.get());
        } else {
            throw new RuntimeException(ErrorMessagesEnum.AUTH_ERROR.getErrorMessage());
        }
    }

}
