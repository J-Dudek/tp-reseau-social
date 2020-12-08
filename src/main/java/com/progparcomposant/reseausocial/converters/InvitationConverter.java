package com.progparcomposant.reseausocial.converters;

import com.progparcomposant.reseausocial.dto.InvitationDTO;
import com.progparcomposant.reseausocial.model.Invitation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvitationConverter {

    public InvitationDTO entityToDto(Invitation invitation) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(invitation, InvitationDTO.class);
    }

    public List<InvitationDTO> entityToDto(List<Invitation> invitations) {
        return invitations.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Invitation dtoToEntity(InvitationDTO invitationDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(invitationDTO, Invitation.class);
    }

    public List<Invitation> dtoToEntity(List<InvitationDTO> invitationsDTO) {
        return invitationsDTO.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }
}
