package com.orion.eventos_service.Controller;

import com.orion.eventos_service.Assembler.EventoModelAssembler;
import com.orion.eventos_service.DTO.EventoRequest;
import com.orion.eventos_service.DTO.EventoResponse;
import com.orion.eventos_service.Service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/evento")
public class EventoController {
    @Autowired
    private EventoService service;
    @Autowired
    private EventoModelAssembler assembler;



    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EventoResponse>> crear(@RequestBody EventoRequest dto) {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        EventoResponse nuevo = service.guardar(dto, (Long) auth.getCredentials());
        return ResponseEntity
                .created(linkTo(methodOn(EventoController.class).verEvento(nuevo.getIdEvento())).toUri())
                .body(assembler.toModel(nuevo));
    }



    @GetMapping(value = "/ver/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<EventoResponse> verEvento(@PathVariable Long id) {
        EventoResponse evento = service.obtenerPorId(id);
        return assembler.toModel(evento);
    }




    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<EventoResponse>> verEventosGlobales() {
        List<EntityModel<EventoResponse>> lista = service.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(lista,
                linkTo(methodOn(EventoController.class).verEventosGlobales()).withSelfRel());
    }






    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        service.eliminar(id,(Long) auth.getCredentials());

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/actualizarevento/{id}")
    public ResponseEntity<EntityModel<EventoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequest dto) {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        EventoResponse actualizado = service.actualizar(id, dto, (Long) auth.getCredentials());
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }


}
