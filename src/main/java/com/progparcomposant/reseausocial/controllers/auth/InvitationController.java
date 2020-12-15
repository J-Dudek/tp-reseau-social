package com.progparcomposant.reseausocial.controllers.auth;

import com.progparcomposant.reseausocial.configuration.SwaggerConfig;
import com.progparcomposant.reseausocial.dto.InvitationDTO;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.services.InvitationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/invitations")
@Api(tags = { SwaggerConfig.INVITATION })
public class InvitationController {

    private final InvitationService invitationService;

    @GetMapping(path = "/all")
    @ApiOperation(value = "For test only.")
    public List<InvitationDTO> findAllInvitations() {
        try {
            return this.invitationService.findAllInvitations();
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{firstUserId}/{secondUserId}")
    @ApiOperation(value = "Find out if there is an invitation between two users.")
    public InvitationDTO findInvitationByUserRequestedId(@PathVariable(name = "firstUserId") Long firstUserId, @PathVariable(name = "secondUserId") Long secondUserId) {
        try {
            return this.invitationService.findInvitationByUserRequestedId(firstUserId, secondUserId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{userId}/all")
    @ApiOperation(value = "Find all invitations by user.")
    public List<InvitationDTO> findAllInvitationsByUserId(@PathVariable(name = "userId") Long userId) {
        try {
            return this.invitationService.findAllInvitationsByUserId(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    @ApiOperation(value = "Create a new invitation.")
    public InvitationDTO createInvitation(@RequestBody InvitationDTO newInvitationDTO) {
        try {
            return this.invitationService.createInvitation(newInvitationDTO);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/{firstUserId}/accept/{secondUserId}")
    @ApiOperation(value = "For accept an invitation, delete after accept.")
    public void acceptInvitationByUserIds(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        try {
            this.invitationService.acceptInvitationByUserIds(firstUserId, secondUserId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/{userId}/accept/all")
    @ApiOperation(value = "When the user wishes to accept all the invitations he has pending.")
    public void acceptAllInvitations(@PathVariable("userId") Long userId) {
        try {
            this.invitationService.acceptAllInvitations(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{firstUserId}/delete/{secondUserId}")
    @ApiOperation(value = "Delete an invitation between 2 user, if a user want to cancel his proposition for example.")
    public void deleteInvitationByUserIds(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        try {
            this.invitationService.deleteInvitationByUserIds(firstUserId, secondUserId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{userId}/delete/all")
    @ApiOperation(value="When a user want to delete all invitation in pending.")
    public void deleteAllInvitationsByUserId(@PathVariable("userId") Long userId) {
        try {
            this.invitationService.deleteAllInvitationsByUserId(userId);
        } catch (SocialNetworkException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
