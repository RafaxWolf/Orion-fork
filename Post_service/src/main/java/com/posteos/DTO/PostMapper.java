package com.posteos.DTO;

import com.posteos.Entity.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostMapper {
    //convierte los datos de request a una entidad Post para guardar en la base de datos
    public Post aEntidad(PostRequestDTO pdto)
    {
        Post post = new Post(); //Se crea una entidad
        post.setUserId(pdto.getUserId());
        post.setContent(pdto.getContent());
        post.setMediaUrl(pdto.getMediaUrl());

        return post;


    }
    // De la base de datos  A afuera//
    public PostResponseDTO response(Post posteo){
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(posteo.getId());
        dto.setContent(posteo.getContent());
        dto.setUserId(posteo.getUserId());
        dto.setMediaUrl(posteo.getMediaUrl());
        dto.setCreado_el(posteo.getCreadoEl());
        return dto;

    }
}
