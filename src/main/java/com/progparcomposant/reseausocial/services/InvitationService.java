package com.progparcomposant.reseausocial.services;

import com.progparcomposant.reseausocial.converters.InvitationConverter;
import com.progparcomposant.reseausocial.dto.FriendshipDTO;
import com.progparcomposant.reseausocial.dto.InvitationDTO;
import com.progparcomposant.reseausocial.exceptions.InvitationException;
import com.progparcomposant.reseausocial.exceptions.SocialNetworkException;
import com.progparcomposant.reseausocial.exceptions.errors.ErrorMessagesEnum;
import com.progparcomposant.reseausocial.model.Invitation;
import com.progparcomposant.reseausocial.repositories.InvitationRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@AllArgsConstructor
@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final InvitationConverter invitationConverter;
    private final FriendshipService friendshipService;

    public List<InvitationDTO> findAllInvitations() {
        Iterable<Invitation> invitations = this.invitationRepository.findAll();
        if (IterableUtils.size(invitations) > 0) {
            return this.invitationConverter.entityToDto(IterableUtils.toList(invitations));
        } else {
            throw new InvitationException(ErrorMessagesEnum.INVITATION_NO_INVITATIONS_IN_DATABASE.getErrorMessage());
        }
    }

    public InvitationDTO createInvitation(InvitationDTO newInvitationDTO) {
        return invitationConverter.entityToDto(this.invitationRepository.save(invitationConverter.dtoToEntity(newInvitationDTO)));
    }

    public InvitationDTO findInvitationByUserRequestedId(Long firstUserId, Long secondUserId) {
        try {
            return this.findInvitation(firstUserId, secondUserId);
        } catch (SocialNetworkException ex) {
            throw new InvitationException(ErrorMessagesEnum.INVITATION_NOT_FOUND.getErrorMessage());
        }
    }

    public List<InvitationDTO> findAllInvitationsByUserId(Long userId) {
        Iterable<Invitation> invitations = this.invitationRepository.findAllByFirstUserIdOrSecondUserId(userId, userId);
        if (IterableUtils.size(invitations) > 0) {
            return this.invitationConverter.entityToDto(IterableUtils.toList(invitations));
        } else {
            throw new InvitationException(ErrorMessagesEnum.INVITATION_NO_INVITATION_YET.getErrorMessage());
        }
    }

    public void acceptInvitationByUserIds(Long firstUserId, Long secondUserId) {
        try {
            InvitationDTO invitationDTO = this.findInvitation(firstUserId, secondUserId);
            FriendshipDTO friendshipDTOtoSave = this.friendshipService.getNewFriendship(invitationDTO.getFirstUserId(), invitationDTO.getSecondUserId());
            this.friendshipService.saveFriendship(friendshipDTOtoSave);
            this.deleteInvitationByUserIds(invitationDTO.getFirstUserId(), invitationDTO.getSecondUserId());
        } catch (SocialNetworkException ex) {
            throw new InvitationException(ErrorMessagesEnum.INVITATION_NOT_FOUND.getErrorMessage());
        }
    }



    public void acceptAllInvitations(@PathVariable("userId") Long userId) {
        Iterable<Invitation> invitations = this.invitationRepository.findAllByFirstUserIdOrSecondUserId(userId, userId);
        if (IterableUtils.size(invitations) > 0) {
            List<InvitationDTO> invitationDTOS = this.invitationConverter.entityToDto(IterableUtils.toList(invitations));
            List<FriendshipDTO> newFriendshipDTOS = new ArrayList<>();
            invitationDTOS.forEach(invitationDTO -> newFriendshipDTOS.add(this.friendshipService.getNewFriendship(invitationDTO.getFirstUserId(), invitationDTO.getSecondUserId())));
            this.friendshipService.saveAllFriendships(newFriendshipDTOS);
            this.deleteAllInvitationsByUserId(userId);
        } else {
            throw new InvitationException(ErrorMessagesEnum.INVITATION_NOT_FOUND.getErrorMessage());
        }
    }

    public void deleteAllInvitationsByUserId(Long userId) {
        this.invitationRepository.deleteInvitationsByFirstUserIdOrSecondUserId(userId, userId);
    }

    public void deleteInvitationByUserIds(Long firstUserId, Long secondUserId) {
        try {
            InvitationDTO invitationDTO = this.findInvitation(firstUserId, secondUserId);
            this.invitationRepository.deleteByFirstUserIdAndSecondUserId(invitationDTO.getFirstUserId(), invitationDTO.getSecondUserId());
        } catch (SocialNetworkException ex) {
            throw new InvitationException(ErrorMessagesEnum.INVITATION_NOT_FOUND.getErrorMessage());
        }
    }

    private InvitationDTO findInvitation(Long firstUserId, Long secondUserId) {
        Optional<Invitation> invitation = this.invitationRepository.findByFirstUserIdAndSecondUserId(firstUserId, secondUserId);
        if (invitation.isPresent()) {
            return this.invitationConverter.entityToDto(invitation.get());
        } else {
            invitation = this.invitationRepository.findByFirstUserIdAndSecondUserId(secondUserId, firstUserId);
            if (invitation.isPresent()) {
                return this.invitationConverter.entityToDto(invitation.get());
            } else {
                throw new InvitationException(ErrorMessagesEnum.INVITATION_NOT_FOUND.getErrorMessage());
            }
        }
    }
}
