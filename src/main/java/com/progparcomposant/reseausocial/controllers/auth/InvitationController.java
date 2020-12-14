package com.progparcomposant.reseausocial.controllers.auth;

import com.progparcomposant.reseausocial.dto.InvitationDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.services.InvitationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    @GetMapping(path = "/all")
    public List<InvitationDTO> findAllInvitations() {
        try {
            return this.invitationService.findAllInvitations();
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{firstUserId}/{secondUserId}")
    public InvitationDTO findInvitationByUserRequestedId(@PathVariable(name = "firstUserId") Long firstUserId, @PathVariable(name = "secondUserId") Long secondUserId) {
        try {
            return this.invitationService.findInvitationByUserRequestedId(firstUserId, secondUserId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{userId}/all")
    public List<InvitationDTO> findAllInvitationsByUserId(@PathVariable(name = "userId") Long userId) {
        try {
            return this.invitationService.findAllInvitationsByUserId(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    public InvitationDTO createInvitation(@RequestBody InvitationDTO newInvitationDTO) {
        try {
            return this.invitationService.createInvitation(newInvitationDTO);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/{firstUserId}/accept/{secondUserId}")
    public void acceptInvitationByUserIds(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        try {
            this.invitationService.acceptInvitationByUserIds(firstUserId, secondUserId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/{userId}/accept/all")
    public void acceptAllInvitations(@PathVariable("userId") Long userId) {
        try {
            this.invitationService.acceptAllInvitations(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{firstUserId}/delete/{secondUserId}")
    public void deleteInvitationByUserIds(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        try {
            this.invitationService.deleteInvitationByUserIds(firstUserId, secondUserId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{userId}/delete/all")
    public void deleteAllInvitationsByUserId(@PathVariable("userId") Long userId) {
        try {
            this.invitationService.deleteAllInvitationsByUserId(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
