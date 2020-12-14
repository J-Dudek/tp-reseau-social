package com.progparcomposant.reseausocial.controllers.open;

import com.fasterxml.jackson.annotation.JsonView;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.exceptions.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import com.progparcomposant.reseausocial.security.WebSecurity;
import com.progparcomposant.reseausocial.views.UserViews;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping(path = "/account")
public class AuthenticationController {

    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${auth0.audience}")
    private  String audience;
    @Value("${auth0.id}")
    private  String id;
    @Value("${auth0.secret}")
    private  String secret;
    @Value("${auth0.grant}")
    private  String grant;
    @Value("${auth0.uri}")
    private  String uri;
    public AuthenticationController(UserConverter userConverter,UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userConverter=userConverter;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }
    
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

    @PostMapping("/getToken")
    public String getToken() throws UnirestException {
        return Unirest.post(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("client_id", id)
                .field("client_secret", secret)
                .field("audience", audience)
                .field("grant_type", grant)
                .asJson().getBody().getObject().getString("access_token");
    }




}