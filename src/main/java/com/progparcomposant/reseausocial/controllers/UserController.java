package com.progparcomposant.reseausocial.controllers;


import com.progparcomposant.reseausocial.controllers.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserController(UserConverter userConverter,UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @GetMapping
    public List<UserDTO> findUsers() {
        Iterable<User> users = this.userRepository.findAll();
        if (IterableUtils.size(users) > 0) {
            return this.userConverter.entityToDto(IterableUtils.toList(users));
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.USER_NO_USERS_IN_DATABASE.getErrorMessage());
        }
    }

    @GetMapping(path = "/{userId}")
    public UserDTO findUserById(@PathVariable("userId") Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    @GetMapping(path = "/name/{userName}")
    public UserDTO findUserByName(@PathVariable("userName") String userName){
        Optional<User> user = this.userRepository.findUsersByUsername(userName);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.USER_NO_USER_WITH_THAT_NAME.getErrorMessage());
        }
    }

    @GetMapping(path = "/email/{email}")
    public UserDTO findUserByEmail( @PathVariable("email") String email){
        Optional<User> user = this.userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new NoSuchElementException("Aucun user avec cet email");
        }
    }

    @GetMapping(path = "/phone/{phoneNumber}")
    public UserDTO findUserByPhoneNumber( @PathVariable("phoneNumber") String phoneNumber){
        Optional<User> user = this.userRepository.findUserByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new NoSuchElementException("Aucun user avec cenuméro de téléphone");
        }
    }

    @PostMapping
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
            throw new NoSuchElementException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    @DeleteMapping(path = "/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId){
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            this.userRepository.deleteById(userId);
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    @DeleteMapping
    public void deleteAllUsers(){ this.userRepository.deleteAll();}

    @DeleteMapping(path = "/list/{listIds}")
    public void deleteUsersByIds(@PathVariable("listIds") List<Long> listIds){
        for(Long id : listIds) {
            this.userRepository.deleteById(id);
        }
    }
}
