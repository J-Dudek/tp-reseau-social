package com.progparcomposant.reseausocial.controllers;

import com.progparcomposant.reseausocial.controllers.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.converters.FriendshipConverter;
import com.progparcomposant.reseausocial.converters.InvitationConverter;
import com.progparcomposant.reseausocial.dto.FriendshipDTO;
import com.progparcomposant.reseausocial.dto.InvitationDTO;
import com.progparcomposant.reseausocial.model.Invitation;
import com.progparcomposant.reseausocial.repositories.FriendshipRepository;
import com.progparcomposant.reseausocial.repositories.InvitationRepository;
import javassist.NotFoundException;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping(path = "/invitations")
public class InvitationController {

    private final InvitationRepository invitationRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipConverter friendshipConverter;
    private final InvitationConverter invitationConverter;

    public InvitationController(InvitationRepository invitationRepository, FriendshipRepository friendshipRepository, FriendshipConverter friendshipConverter, InvitationConverter invitationConverter) {
        this.invitationRepository = invitationRepository;
        this.friendshipRepository = friendshipRepository;
        this.friendshipConverter = friendshipConverter;
        this.invitationConverter = invitationConverter;
    }

    @GetMapping
    public List<InvitationDTO> findAllInvitations() {
        Iterable<Invitation> invitations = this.invitationRepository.findAll();

        if (IterableUtils.size(invitations) > 0) {
            return this.invitationConverter.entityToDto(IterableUtils.toList(invitations));
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.INVITATION_NO_INVITATIONS_IN_DATABASE.getErrorMessage());
        }
    }

    @PostMapping(path = "/new")
    public InvitationDTO newInvitation(@RequestBody InvitationDTO newInvitationDTO) {
        return invitationConverter.entityToDto(this.invitationRepository.save(invitationConverter.dtoToEntity(newInvitationDTO)));
    }

    @GetMapping(path = "/{invitationId}")
    public InvitationDTO findInvitationById(@PathVariable(name = "invitationId") Long invitationId) {
        Optional<Invitation> invitation = this.invitationRepository.findById(invitationId);
        if (invitation.isPresent()) {
            return this.invitationConverter.entityToDto(invitation.get());
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.INVITATION_NOT_FOUND.getErrorMessage());
        }
    }

    @GetMapping(path = "/{userId}")
    public List<InvitationDTO> findAllInvitationsByUserId(@PathVariable(name = "userId") Long userId) {
        Iterable<Invitation> invitations = this.invitationRepository.findAllByFirstUserId(userId);
        if (IterableUtils.size(invitations) > 0) {
            return this.invitationConverter.entityToDto(IterableUtils.toList(invitations));
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.INVITATION_NO_INVITATION_YET.getErrorMessage());
        }
    }

    @PutMapping(path = "/accept/{firstUserId}/{secondUserId}")
    public void acceptInvitationById(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) throws NotFoundException {
        Optional<Invitation> invitation = this.invitationRepository.findByFirstUserIdAndSecondUserId(firstUserId, secondUserId);
        if (invitation.isPresent()) {
            InvitationDTO invitationDTO = invitationConverter.entityToDto(invitation.get());
            FriendshipDTO friendshipDTO = new FriendshipDTO(invitationDTO.getFirstUserId(), invitationDTO.getSecondUserId(), new Timestamp(Calendar.getInstance().getTimeInMillis()));
            this.friendshipRepository.save(friendshipConverter.dtoToEntity(friendshipDTO));
            this.invitationRepository.deleteByFirstUserIdAndSecondUserId(invitationDTO.getFirstUserId(), invitationDTO.getSecondUserId());
        } else {
            throw new NotFoundException(ErrorMessagesEnum.INVITATION_NOT_FOUND.getErrorMessage());
        }
    }

    @PutMapping(path = "/accept/all/{userId}")
    public void acceptAllInvitations(@PathVariable("userId") Long userId) {
        Iterable<Invitation> invitations = this.invitationRepository.findAllByFirstUserId(userId);
        if (IterableUtils.size(invitations) > 0) {
            List<InvitationDTO> invitationDTOS = this.invitationConverter.entityToDto(IterableUtils.toList(invitations));
            List<FriendshipDTO> newFriendshipDTOS = new ArrayList<>();
            invitationDTOS.forEach(invitationDTO -> newFriendshipDTOS.add(new FriendshipDTO(invitationDTO.getFirstUserId(), invitationDTO.getSecondUserId(), new Timestamp(Calendar.getInstance().getTimeInMillis()))));
            this.friendshipRepository.saveAll(this.friendshipConverter.dtoToEntity(newFriendshipDTOS));
            this.invitationRepository.deleteAllByFirstUserId(userId);
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.INVITATION_NOT_FOUND.getErrorMessage());
        }
    }

    @DeleteMapping(path = "/delete/{firstUserId}/{secondUserId}")
    public void deleteInvitationById(@PathVariable("firstUserId") Long firstUserId, @PathVariable("secondUserId") Long secondUserId) {
        Optional<Invitation> invitation = this.invitationRepository.findByFirstUserIdAndSecondUserId(firstUserId, secondUserId);
        if (invitation.isPresent()) {
            this.invitationRepository.deleteByFirstUserIdAndSecondUserId(firstUserId, secondUserId);
        } else {
            throw new NoSuchElementException(ErrorMessagesEnum.INVITATION_NOT_FOUND.getErrorMessage());
        }
    }

    @DeleteMapping(path = "/delete/all/{userId}")
    public void deleteAllInvitationsByUserId(@PathVariable("userId") Long userId) {
        this.invitationRepository.deleteAllByFirstUserIdOrSecondUserId(userId, userId);
    }
}
