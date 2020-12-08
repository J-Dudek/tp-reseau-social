package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import javassist.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/")
public class MainController {

    private final UserConverter userConverter;
    private final UserRepository userRepository;

    public MainController(UserConverter userConverter, UserRepository userRepository) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public void register(@RequestBody UserDTO userDTO) throws Exception {
        if(userRepository.findUserByEmail(userDTO.getEmail()).isPresent()){
            throw new Exception("Email already assigned to an account");
        }else{
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            this.userRepository.save(this.userConverter.dtoToEntity(userDTO));
        }
    }

    @PostMapping("/{email:.+}/{password}")
    public void login(@PathVariable String email, @PathVariable String password, HttpServletRequest request) throws Exception {

        userRepository.findUserByEmail(email).ifPresent(userConverter::entityToDto);
        if(userRepository.findUserByEmail(email).isPresent()){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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
