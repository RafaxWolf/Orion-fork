package com.posteos.Service;

import com.posteos.DTO.PostMapper;
import com.posteos.DTO.PostRequestDTO;
import com.posteos.DTO.PostResponseDTO;
import com.posteos.Entity.Post;
import com.posteos.Exception.ResourceNotFoundException;
import com.posteos.Repository.Repository_Post;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicePost {

    private static final Logger log = LoggerFactory.getLogger(ServicePost.class);

    private final Repository_Post repo;
    private final PostMapper mapper;

    @Transactional
    public PostResponseDTO guardar(PostRequestDTO dto) {
        log.info("Guardando nuevo post para usuario id={}", dto.getUserId());
        Post post = mapper.aEntidad(dto);
        Post guardado = repo.save(post);
        log.info("Post guardado con id={}", guardado.getId());
        return mapper.response(guardado);
    }

    @Transactional(readOnly = true)
    public PostResponseDTO buscarPorId(Long id) {
        log.info("Buscando post con id={}", id);
        Post post = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));
        return mapper.response(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los posts");
        return repo.findAll()
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }


    public List<PostResponseDTO> buscarPostDeUsuario(Long userId) {

        List<Post> posts = repo.findByUserIdOrderByCreadoElDesc(userId);

        return posts.stream()
                .map(post -> new PostResponseDTO(
                        post.getId(),
                        post.getUserId(),
                        post.getContent(),
                        post.getMediaUrl(),
                        post.getCreadoEl()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> buscarUser(Long id) {
        log.info("Buscando posts del usuario id={}", id);
        return repo.findByUserIdOrderByCreadoElDesc(id)
                .stream()
                .map(mapper::response)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDTO actualizar(Long id, PostRequestDTO dto) {
        log.info("Actualizando post id={}", id);
        Post post = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));
        post.setContent(dto.getContent());
        post.setMediaUrl(dto.getMediaUrl());
        Post actualizado = repo.save(post);
        log.info("Post id={} actualizado correctamente", id);
        return mapper.response(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando post id={}", id);
        repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));
        repo.deleteById(id);
        log.info("Post id={} eliminado correctamente", id);
    }


}