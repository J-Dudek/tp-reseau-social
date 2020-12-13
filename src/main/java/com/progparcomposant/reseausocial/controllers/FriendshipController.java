package com.progparcomposant.reseausocial.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.progparcomposant.reseausocial.dto.FriendshipDTO;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.services.FriendshipService;
import com.progparcomposant.reseausocial.views.UserViews;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @GetMapping("/all")
    // @JsonView(UserViews.Friends.class)
    public List<FriendshipDTO> findAllFriendships() {
        return this.friendshipService.findAllFriendships();
    }

    @GetMapping(path = "/{userId}")
    @JsonView(UserViews.Friends.class)
    public List<UserDTO> findFriendsByUserId(@PathVariable("userId") Long userId) {
        return this.friendshipService.findFriendsByUserId(userId);
    }

    @GetMapping(path = "/{firstUserId}/{secondUserId}")
    @JsonView(UserViews.Friends.class)
    public UserDTO findFriendByFriendId(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        return this.friendshipService.findFriendByFriendId(firstUserId, secondUserId);
    }

    @DeleteMapping(path = "/{firstUserId}/delete/{secondUserId}")
    public void deleteFriendByFriendId(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        this.friendshipService.deleteFriendByFriendId(firstUserId, secondUserId);
    }

    @DeleteMapping(path = "/{userId}/delete/all")
    public void deleteAllUserFriends(@PathVariable("userId") Long userId) {
        this.friendshipService.deleteAllUserFriends(userId);
    }
}
