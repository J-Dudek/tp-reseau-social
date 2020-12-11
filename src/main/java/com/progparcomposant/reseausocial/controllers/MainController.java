package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import javassist.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/")
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
            this.userRepository.save(this.userConverter.dtoToEntity(userDTO));
        }
    }

    @PostMapping("/{email:.+}/{password}")
    public void login(@PathVariable String email, @PathVariable String password, HttpServletRequest request) throws Exception {

        userRepository.findUserByEmail(email).ifPresent(userConverter::entityToDto);
        if(userRepository.findUserByEmail(email).isPresent()){
            UserDTO userDTO = userConverter.entityToDto(userRepository.findUserByEmail(email).get());
            if(passwordEncoder.matches(password,userDTO.getPassword())){
                request.getSession().setAttribute("userId", userDTO.getIdUser());
            }else{
                throw new NotFoundException("Utilisateur non trouvé");
            }
        }else{
            throw new Exception("Erreur d'identification");
        }
    }

}
