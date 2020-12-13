package com.progparcomposant.reseausocial.services;

import com.progparcomposant.reseausocial.converters.FriendshipConverter;
import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.FriendshipDTO;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.exceptions.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.model.Friendship;
import com.progparcomposant.reseausocial.repositories.FriendshipRepository;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FriendshipService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipConverter friendshipConverter;
    private final UserService userService;

    public boolean isFriendshipExists(Long firstUserId, Long secondUserId) {
        if (this.friendshipRepository.findByFirstUserIdAndSecondUserId(firstUserId, secondUserId).isPresent()) {
            return true;
        } else {
            return this.friendshipRepository.findByFirstUserIdAndSecondUserId(secondUserId, firstUserId).isPresent();
        }
    }

    public FriendshipDTO getNewFriendship(Long firstUserId, Long secondUserId) {
        return new FriendshipDTO(firstUserId, secondUserId, new Timestamp(Calendar.getInstance().getTimeInMillis()));
    }
    public FriendshipDTO saveFriendship(FriendshipDTO friendshipDTO) {
        return this.friendshipConverter.entityToDto(this.friendshipRepository.save(friendshipConverter.dtoToEntity(friendshipDTO)));
    }

    public List<FriendshipDTO> saveAllFriendships(List<FriendshipDTO> friendshipDTOS) {
        Iterable<Friendship> friendships = this.friendshipRepository.saveAll((Iterable<Friendship>) this.friendshipConverter.dtoToEntity(friendshipDTOS));
        return this.friendshipConverter.entityToDto(IterableUtils.toList(friendships));
    }

    public List<FriendshipDTO> findAllFriendships() {
        Iterable<Friendship> friendships = this.friendshipRepository.findAll();
        if (IterableUtils.size(friendships) > 0) {
            return this.friendshipConverter.entityToDto(IterableUtils.toList(friendships));
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.FRIENDSHIP_NO_FRIENDSHIPS_IN_DATABASE.getErrorMessage());
        }
    }

    public List<UserDTO> findFriendsByUserId(@PathVariable("userId") Long userId) {
        Iterable<Friendship> friendships = this.friendshipRepository.findFriendshipsByFirstUserIdOrSecondUserId(userId, userId);
        if (IterableUtils.size(friendships) > 0) {
            List<FriendshipDTO> friendshipDTOS = this.friendshipConverter.entityToDto(IterableUtils.toList(friendships));
            List<Long> friendsIds = this.guessFriendIds(friendshipDTOS, userId);
            return this.userConverter.entityToDto(this.userRepository.findByIdIn(friendsIds));
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage());
        }
    }

    public UserDTO findFriendByFriendId(Long firstUserId, Long secondUserId) {
        try {
            FriendshipDTO friendshipDTO = this.findFriendship(firstUserId, secondUserId);
            Long friendId = this.guessFriendId(friendshipDTO, firstUserId);
            return this.userService.findUserByUserId(friendId);
        } catch (SocialNetworkException exception) {
            throw new SocialNetworkException(ErrorMessagesEnum.FRIENDSHIP_NOT_FOUND.getErrorMessage());
        }
    }

    public void deleteFriendByFriendId(Long firstUserId, Long secondUserId) {
        try {
            FriendshipDTO friendshipDTO = this.findFriendship(firstUserId, secondUserId);
            this.friendshipRepository.deleteByFirstUserIdAndSecondUserId(friendshipDTO.getFirstUserId(), friendshipDTO.getSecondUserId());
        } catch (SocialNetworkException ex) {
            throw new SocialNetworkException(ErrorMessagesEnum.FRIENDSHIP_NOT_FOUND.getErrorMessage());
        }
    }

    public void deleteAllUserFriends(Long userId) {
        Iterable<Friendship> friendships = this.friendshipRepository.findFriendshipsByFirstUserIdOrSecondUserId(userId, userId);
        if (IterableUtils.size(friendships) > 0) {
            this.friendshipRepository.deleteFriendshipsByFirstUserIdOrSecondUserId(userId, userId);
        } else {
            throw new SocialNetworkException(ErrorMessagesEnum.FRIENDSHIP_USER_WITH_NO_FRIENDS.getErrorMessage());
        }
    }

    private FriendshipDTO findFriendship(Long firstUserId, Long secondUserId) {
        Optional<Friendship> friendship = this.friendshipRepository.findByFirstUserIdAndSecondUserId(firstUserId, secondUserId);
        if (friendship.isPresent()) {
            return this.friendshipConverter.entityToDto(friendship.get());
        } else {
            friendship = this.friendshipRepository.findByFirstUserIdAndSecondUserId(secondUserId, firstUserId);
            if (friendship.isPresent()) {
                return this.friendshipConverter.entityToDto(friendship.get());
            } else {
                throw new SocialNetworkException(ErrorMessagesEnum.FRIENDSHIP_NOT_FOUND.getErrorMessage());
            }
        }
    }

    private List<Long> guessFriendIds(List<FriendshipDTO> friendshipDTOS, Long firstUserId) {
        List<Long> friendIds = new ArrayList<>();
        for (FriendshipDTO friendshipDTO : friendshipDTOS) {
            friendIds.add(this.guessFriendId(friendshipDTO, firstUserId));
        }
        return friendIds;
    }

    private Long guessFriendId(FriendshipDTO friendshipDTO, Long firstUserId) {
        return friendshipDTO.getFirstUserId().equals(firstUserId) ? friendshipDTO.getFirstUserId() : friendshipDTO.getSecondUserId();
    }
}
