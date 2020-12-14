package com.progparcomposant.reseausocial.controllers.auth;

import com.fasterxml.jackson.annotation.JsonView;
import com.progparcomposant.reseausocial.dto.FriendshipDTO;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.services.FriendshipService;
import com.progparcomposant.reseausocial.views.UserViews;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @GetMapping("/all")
    @JsonView(UserViews.Friends.class)
    public List<FriendshipDTO> findAllFriendships() {
        return this.friendshipService.findAllFriendships();
    }

    @GetMapping(path = "/{userId}/all")
    @JsonView(UserViews.Friends.class)
    public List<UserDTO> findFriendsByUserId(@PathVariable("userId") Long userId) {
        try {
            return this.friendshipService.findFriendsByUserId(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @GetMapping(path = "/{firstUserId}/{secondUserId}")
    @JsonView(UserViews.Friends.class)
    public UserDTO findFriendByFriendId(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        try {
            return this.friendshipService.findFriendByFriendId(firstUserId, secondUserId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{firstUserId}/delete/{secondUserId}")
    public void deleteFriendByFriendId(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        try {
            this.friendshipService.deleteFriendByFriendId(firstUserId, secondUserId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @DeleteMapping(path = "/{userId}/delete/all")
    public void deleteAllUserFriends(@PathVariable("userId") Long userId) {
        try {
            this.friendshipService.deleteAllUserFriends(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
