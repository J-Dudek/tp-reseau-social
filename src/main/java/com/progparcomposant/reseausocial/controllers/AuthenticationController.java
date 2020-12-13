package com.progparcomposant.reseausocial.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.exceptions.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import com.progparcomposant.reseausocial.views.UserViews;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/account")
public class AuthenticationController {

    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public void register(@RequestBody UserDTO userDTO) {
        if (userRepository.findUserByEmail(userDTO.getEmail()).isPresent()) {
            throw new SocialNetworkException(ErrorMessagesEnum.AUTH_EMAIL_ALREADY_ASSIGNED.getErrorMessage());
        } else {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userDTO.setSignInDate(new Timestamp(System.currentTimeMillis()));
            this.userRepository.save(this.userConverter.dtoToEntity(userDTO));
        }
    }

    @PostMapping("/login")
    @JsonView(UserViews.Private.class)
    public UserDTO login(@RequestBody UserDTO userDTO) {
        Optional<User> user = userRepository.findUserByUsername(userDTO.getUsername());
        if (user.isPresent() && passwordEncoder.matches(userDTO.getPassword(), user.get().getPassword())) {
            return userConverter.entityToDto(user.get());
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.AUTH_ERROR.getErrorMessage());
        }
    }

}
