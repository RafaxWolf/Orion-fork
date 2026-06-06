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

    //Crear Evento
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EventoResponse>> crear(@Valid @RequestBody EventoRequest dto) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getPrincipal().toString());

        EventoResponse nuevo = service.guardar(dto, userId);

        return ResponseEntity
                .created(linkTo(methodOn(EventoController.class).verEvento(nuevo.getIdEvento())).toUri())
                .body(assembler.toModel(nuevo));
    }

    //Unirse a evento
    @PostMapping("/{id}/unirse")
    public ResponseEntity<EntityModel<EventoResponse>> unirse(@PathVariable Long id) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getPrincipal().toString());

        EventoResponse actualizado = service.unirseAEvento(id, userId);

        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

   //Buscar evento por id
    @GetMapping(value = "/ver/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<EventoResponse> verEvento(@PathVariable Long id) {
        EventoResponse evento = service.obtenerPorId(id);
        return assembler.toModel(evento);
    }

    //VER TODOS LOS EEVENTOS
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<EventoResponse>> verEventosGlobales() {
        List<EntityModel<EventoResponse>> lista = service.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(lista,
                linkTo(methodOn(EventoController.class).verEventosGlobales()).withSelfRel());
    }
    //Borrar evento
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getPrincipal().toString());
        service.eliminar(id, userId);

        return ResponseEntity.noContent().build();
    }

  //Actualizar evento
    @PutMapping("/actualizarevento/{id}")
    public ResponseEntity<EntityModel<EventoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequest dto) {

        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getPrincipal().toString());
        EventoResponse actualizado = service.actualizar(id, dto, userId);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }
}