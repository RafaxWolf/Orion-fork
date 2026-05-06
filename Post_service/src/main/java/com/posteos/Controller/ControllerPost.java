package com.posteos.Controller;

import com.posteos.DTO.PostRequestDTO;
import com.posteos.DTO.PostResponseDTO;
import com.posteos.Service.ServicePost;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
//@CrossOrigin(origins = "http://localhost:5173")
public class ControllerPost {

    private static final Logger log = LoggerFactory.getLogger(ControllerPost.class);

    @Autowired
    private ServicePost service;

    @PostMapping
    public ResponseEntity<PostResponseDTO> guardar(@Valid @RequestBody PostRequestDTO dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        log.info("POST /api/posts - Creando post para usuario id={}", dto.getUserId());
        dto.setUserId(userId);
        PostResponseDTO response = service.guardar(dto);
        log.info("Post creado con id={}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        log.info("GET /api/posts - Obteniendo todos los posts");
        List<PostResponseDTO> lista = service.obtenerTodos();
        if (lista.isEmpty()) {
            log.warn("No hay posts registrados");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO HAY POSTS");
        }
        log.info("Se retornaron {} posts", lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDTO>> obtenerTodosPorUsuario(@PathVariable Long userId) {
        List<PostResponseDTO> posts = service.buscarPostDeUsuario(userId);

        if(posts.isEmpty()) {
            return ResponseEntity.notFound().build(); // devuelve code 204 no hay posts
        }

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/posts/{} - Buscando post por id", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> verPostsUser(@PathVariable Long id) {
        log.info("GET /api/posts/usuario/{} - Posts del usuario", id);
        List<PostResponseDTO> lista = service.buscarUser(id);
        if (lista.isEmpty()) {
            log.warn("No se encontraron posts para usuario id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sin posts para ese usuario");
        }
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDTO dto) {
        log.info("PUT /api/posts/{} - Actualizando post", id);
        PostResponseDTO actualizado = service.actualizar(id, dto);
        log.info("Post id={} actualizado correctamente", id);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrar(@PathVariable Long id) {
        log.info("DELETE /api/posts/{} - Eliminando post", id);
        service.eliminar(id);
        log.info("Post id={} eliminado correctamente", id);
        return ResponseEntity.ok("Post eliminado correctamente");
    }
}