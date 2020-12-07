package com.progparcomposant.reseausocial.converters;

import com.progparcomposant.reseausocial.dto.PostDTO;
import com.progparcomposant.reseausocial.model.Post;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostConverter {

    public PostDTO entityToDto(Post post) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(post, PostDTO.class);
    }

    public List<PostDTO> entityToDto(List<Post> posts) {
        return posts.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Post dtoToEntity(PostDTO postDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(postDTO, Post.class);
    }

    public List<Post> dtoToEntity(List<PostDTO> postsDTO) {
        return postsDTO.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }
}
