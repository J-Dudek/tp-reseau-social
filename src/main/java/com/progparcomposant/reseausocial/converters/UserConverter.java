package com.progparcomposant.reseausocial.converters;

import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    public UserDTO entityToDto(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDTO.class);
    }

    public List<UserDTO> entityToDto(List<User> users) {
        return users.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public User dtoToEntity(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDTO, User.class);
    }

    public List<User> dtoToEntity(List<UserDTO> usersDTO) {
        return usersDTO.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }
}
