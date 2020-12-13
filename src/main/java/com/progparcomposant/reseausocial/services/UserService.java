package com.progparcomposant.reseausocial.services;

import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.exceptions.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.model.User;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserDTO findUserBydId(Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            return this.userConverter.entityToDto(user.get());
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }
}
