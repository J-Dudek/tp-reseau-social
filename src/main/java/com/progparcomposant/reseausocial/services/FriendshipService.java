package com.progparcomposant.reseausocial.services;

import com.progparcomposant.reseausocial.converters.FriendshipConverter;
import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.repositories.FriendshipRepository;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FriendshipService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipConverter friendshipConverter;

    public FriendshipService(UserRepository userRepository, UserConverter userConverter, FriendshipRepository friendshipRepository, FriendshipConverter friendshipConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.friendshipRepository = friendshipRepository;
        this.friendshipConverter = friendshipConverter;
    }


    public boolean isFriendshipExists(Long firstUserId, Long secondUserId) {
        if (this.friendshipRepository.findByFirstUserIdAndSecondUserId(firstUserId, secondUserId).isPresent()) {
            return true;
        } else {
            return this.friendshipRepository.findByFirstUserIdAndSecondUserId(secondUserId, firstUserId).isPresent();
        }
    }
}
