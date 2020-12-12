package com.progparcomposant.reseausocial.controllers;

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
import java.util.NoSuchElementException;
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
    public void register(@RequestBody UserDTO userDTO) throws Exception {
        if(userRepository.findUserByEmail(userDTO.getEmail()).isPresent()){
            throw new Exception("Email already assigned to an account");
        }else{
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userDTO.setSignInDate(new Timestamp(System.currentTimeMillis()));
            this.userRepository.save(this.userConverter.dtoToEntity(userDTO));
        }
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody UserDTO userDTO) throws Exception {
        Optional<User> user= userRepository.findUserByUsername(userDTO.getUsername());
        if(user.isPresent()){
            if(passwordEncoder.matches(userDTO.getPassword(),user.get().getPassword())){
                return userConverter.entityToDto(user.get());
            }
        }
        throw new NoSuchElementException("Erreur d'identification");
    }

}
