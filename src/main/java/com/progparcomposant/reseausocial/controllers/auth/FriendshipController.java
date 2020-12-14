package com.progparcomposant.reseausocial.controllers.auth;

import com.fasterxml.jackson.annotation.JsonView;
import com.progparcomposant.reseausocial.configuration.SwaggerConfig;
import com.progparcomposant.reseausocial.dto.FriendshipDTO;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.services.FriendshipService;
import com.progparcomposant.reseausocial.views.UserViews;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/friends")
@Api(tags = { SwaggerConfig.FRIENDSHIP })
public class FriendshipController {

    private final FriendshipService friendshipService;

    @GetMapping("/all")
    @JsonView(UserViews.Friends.class)
    @ApiOperation(value = "For test only")
    public List<FriendshipDTO> findAllFriendships() {
        return this.friendshipService.findAllFriendships();
    }

    @GetMapping(path = "/{userId}/all")
    @JsonView(UserViews.Friends.class)
    @ApiOperation(value = "list a user's friends")
    public List<UserDTO> findFriendsByUserId(@PathVariable("userId") Long userId) {
        try {
            return this.friendshipService.findFriendsByUserId(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @GetMapping(path = "/{firstUserId}/{secondUserId}")
    @JsonView(UserViews.Friends.class)
    @ApiOperation(value = "list the user's friends of a friend")
    public UserDTO findFriendByFriendId(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        try {
            return this.friendshipService.findFriendByFriendId(firstUserId, secondUserId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{firstUserId}/delete/{secondUserId}")
    @ApiOperation(value = "Delete a friendship relation between two user.")
    public void deleteFriendByFriendId(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        try {
            this.friendshipService.deleteFriendByFriendId(firstUserId, secondUserId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @DeleteMapping(path = "/{userId}/delete/all")
    @ApiOperation(value = "Delete all relationships from a user.")
    public void deleteAllUserFriends(@PathVariable("userId") Long userId) {
        try {
            this.friendshipService.deleteAllUserFriends(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
