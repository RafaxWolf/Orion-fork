package com.Comentarios.Controller;

import com.Comentarios.DTO.RequestComentario;
import com.Comentarios.DTO.ResponseComentario;
import com.Comentarios.Service.ServiceComentario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequiredArgsConstructor
@RequestMapping("/api/comentarios")
public class ControllerComentario {

    private static final Logger log = LoggerFactory.getLogger(ControllerComentario.class);
    private final ServiceComentario service;

    @GetMapping("/{postId}/comentarios")
    public ResponseEntity<List<ResponseComentario>> comentariosPost(
            @PathVariable Long postId) {
        log.info("Get /api/posts/{}/comentarios - ViendoComentarios post", postId);

        List<ResponseComentario> lista = service.buscarPorPost(postId);



        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{postId}/comentar")
    public ResponseEntity<ResponseComentario> comentar(@PathVariable Long postId, @Valid @RequestBody RequestComentario r){
        log.info("POST /api/posts/{}/comentar - Comentando", postId);
        ResponseComentario creado = service.guardar(postId, r);

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);


    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ResponseComentario> actualizar (@PathVariable Long id, @Valid @RequestBody RequestComentario r){
        log.info("POST /api/posts/comentario/{} - Comentando", id);
        ResponseComentario actualizado = service.actualizar(id, r);
        return ResponseEntity.ok(actualizado);


    }



}