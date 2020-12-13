package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.converters.FriendshipConverter;
import com.progparcomposant.reseausocial.converters.InvitationConverter;
import com.progparcomposant.reseausocial.dto.InvitationDTO;
import com.progparcomposant.reseausocial.repositories.FriendshipRepository;
import com.progparcomposant.reseausocial.repositories.InvitationRepository;
import com.progparcomposant.reseausocial.services.InvitationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/invitations")
public class InvitationController {

    private final InvitationRepository invitationRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipConverter friendshipConverter;
    private final InvitationConverter invitationConverter;
    private final InvitationService invitationService;

    @GetMapping(path = "/all")
    public List<InvitationDTO> findAllInvitations() {
        return this.invitationService.findAllInvitations();
    }

    @GetMapping(path = "/{firstUserId}/{secondUserId}")
    public InvitationDTO findInvitationByUserRequestedId(@PathVariable(name = "firstUserId") Long firstUserId, @PathVariable(name = "secondUserId") Long secondUserId) {
        return this.invitationService.findInvitationByUserRequestedId(firstUserId, secondUserId);
    }

    @GetMapping(path = "/{userId}/all")
    public List<InvitationDTO> findAllInvitationsByUserId(@PathVariable(name = "userId") Long userId) {
        return this.invitationService.findAllInvitationsByUserId(userId);
    }

    @PostMapping(path = "/create")
    public InvitationDTO createInvitation(@RequestBody InvitationDTO newInvitationDTO) {
        return this.invitationService.createInvitation(newInvitationDTO);
    }

    @PostMapping(path = "/{firstUserId}/accept/{secondUserId}")
    public void acceptInvitationByUserIds(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        this.invitationService.acceptInvitationByUserIds(firstUserId, secondUserId);
    }

    @PostMapping(path = "/{userId}/accept/all")
    public void acceptAllInvitations(@PathVariable("userId") Long userId) {
        this.invitationService.acceptAllInvitations(userId);
    }

    @DeleteMapping(path = "/{firstUserId}/delete/{secondUserId}")
    public void deleteInvitationByUserIds(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        this.invitationService.deleteInvitationByUserIds(firstUserId, secondUserId);
    }

    @DeleteMapping(path = "/{userId}/delete/all/")
    public void deleteAllInvitationsByUserId(@PathVariable("userId") Long userId) {
        this.invitationService.deleteAllInvitationsByUserId(userId);
    }
}
