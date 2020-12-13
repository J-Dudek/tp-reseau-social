package com.progparcomposant.reseausocial.services;

import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.exceptions.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public List<UserDTO> findAllUsers() {
        Iterable<User> users = this.userRepository.findAll();
        if (IterableUtils.size(users) > 0) {
            return this.userConverter.entityToDto(IterableUtils.toList(users));
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NO_USERS_IN_DATABASE.getErrorMessage());
        }
    }

    public UserDTO findUserByUserId(Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    public UserDTO findUserByName(String userName) {
        Optional<User> user = this.userRepository.findUsersByUsername(userName);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NO_USER_WITH_THAT_NAME.getErrorMessage());
        }
    }

    public UserDTO findUserByEmail(String email) {
        Optional<User> user = this.userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NO_USER_WITH_THAT_EMAIL.getErrorMessage());
        }
    }

    public UserDTO findUserByPhoneNumber(String phoneNumber) {
        Optional<User> user = this.userRepository.findUserByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NO_USER_WITH_THAT_PHONENUMBER.getErrorMessage());
        }
    }

    public UserDTO updateUser(@RequestBody UserDTO newUserDto) {
        try {
            this.findUserByUserId(newUserDto.getIdUser());
            return userConverter.entityToDto(userRepository.save(this.userConverter.dtoToEntity(newUserDto)));
        } catch (SocialNetworkException ex) {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    public UserDTO createUser(UserDTO newUserDto) {
        return userConverter.entityToDto(userRepository.save(this.userConverter.dtoToEntity(newUserDto)));
    }

    public void deleteUserById(Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            this.userRepository.deleteById(userId);
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    public void deleteAllUsers() {
        Iterable<User> users = this.userRepository.findAll();
        if (IterableUtils.size(users) > 0) {
            this.userRepository.deleteAll();
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NO_USERS_IN_DATABASE.getErrorMessage());
        }
    }

    public void deleteUsersByIds(List<Long> userIds) {
        Iterable<User> users = this.userRepository.findByIdIn(userIds);
        if (IterableUtils.size(users) > 0) {
            for (Long id : userIds) {
                this.userRepository.deleteById(id);
            }
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }
}
