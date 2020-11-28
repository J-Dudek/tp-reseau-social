package com.progparcomposant.reseausocial.converters;

import com.progparcomposant.reseausocial.dto.FriendshipDTO;
import com.progparcomposant.reseausocial.model.Friendship;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FriendshipConverter {

    public FriendshipDTO entityToDto(Friendship friendship) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(friendship, FriendshipDTO.class);
    }

    public List<FriendshipDTO> entityToDto(List<Friendship> friendships) {
        return friendships.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Friendship dtoToEntity(FriendshipDTO friendshipDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(friendshipDTO, Friendship.class);
    }

    public List<Friendship> dtoToEntity(List<FriendshipDTO> friendshipsDTO) {
        return friendshipsDTO.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }
}
