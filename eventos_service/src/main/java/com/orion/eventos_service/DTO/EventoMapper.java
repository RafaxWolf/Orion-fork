package com.orion.eventos_service.DTO;

import com.orion.eventos_service.Entity.Evento;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {
    public EventoResponse aResponse(Evento e ){
        EventoResponse dto = new EventoResponse();
        dto.setIdEvento(e.getIdEvento());
        dto.setIdCreador(e.getIdCreador());
        dto.setAsistentes(e.getAsistentes());
        dto.setFecha(e.getFecha());
        dto.setCreadoEl(e.getCreadoEl());
        dto.setLugar(e.getLugar());
        dto.setNombre(e.getNombre());
        if (e.getAsistentes() != null) {
            dto.setCantidadAsistentes(e.getAsistentes().size());
        } else {
            dto.setCantidadAsistentes(0);
        }
        return dto;

    }
    public Evento aEntidad(EventoRequest er, Long creadorId){
        Evento e = new Evento();
        e.setIdCreador(creadorId);
        e.setNombre(er.getNombre());
        e.setLugar(er.getLugar());
        e.setFecha(er.getFecha());

        return e;

    }
}
