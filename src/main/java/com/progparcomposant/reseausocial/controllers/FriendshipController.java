package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.converters.FriendshipConverter;
import com.progparcomposant.reseausocial.dto.FriendshipDTO;
import com.progparcomposant.reseausocial.model.Friendship;
import com.progparcomposant.reseausocial.repositories.FriendshipRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/friends")
public class FriendshipController {

    private final FriendshipRepository friendshipRepository;
    private final FriendshipConverter friendshipConverter;

    public FriendshipController(FriendshipRepository friendshipRepository, FriendshipConverter friendshipConverter) {
        this.friendshipRepository = friendshipRepository;
        this.friendshipConverter = friendshipConverter;
    }

    @GetMapping
    public Iterable<FriendshipDTO> findFriendship() { return this.friendshipConverter.entityToDto((List<Friendship>) this.friendshipRepository.findAll()); }

    @GetMapping(path="/user/{userId}")
    public Iterable<Friendship> findFriendsByUserId(@PathVariable("userId") Long userId){
        return this.friendshipRepository.findFriendshipsByFirstUserIdOrSecondUserId(userId,userId);
    }

    
}
