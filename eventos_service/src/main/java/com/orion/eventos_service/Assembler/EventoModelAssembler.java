package com.orion.eventos_service.Assembler;

import com.orion.eventos_service.Controller.EventoController;
import com.orion.eventos_service.DTO.EventoResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EventoModelAssembler implements RepresentationModelAssembler<EventoResponse, EntityModel<EventoResponse>> {

    @Override
    public EntityModel<EventoResponse> toModel(EventoResponse evento) {
        return EntityModel.of(evento,
                linkTo(methodOn(EventoController.class)
                        .verEvento(evento.getIdEvento())).withSelfRel(),
                linkTo(methodOn(EventoController.class)
                        .verEventosGlobales()).withRel("todos"),
                linkTo(methodOn(EventoController.class)
                        .actualizar(evento.getIdEvento(), null)).withRel("actualizar"),
                linkTo(methodOn(EventoController.class)
                        .borrar(evento.getIdEvento())).withRel("eliminar"),
                linkTo(methodOn(EventoController.class).unirse(evento.getIdEvento())).withRel("unirse")
        );
    }
}