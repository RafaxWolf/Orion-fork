package com.orion.eventos_service.Service;

import com.orion.eventos_service.DTO.EventoMapper;
import com.orion.eventos_service.DTO.EventoRequest;
import com.orion.eventos_service.DTO.EventoResponse;
import com.orion.eventos_service.Entity.Evento;
import com.orion.eventos_service.Repository.RepositoryEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventoService {
    @Autowired
    private  RepositoryEvento repo;
    private  EventoMapper mapper;
    @Transactional
    public EventoResponse guardar(EventoRequest dto) {

        Evento evento = mapper.aEntidad(dto,);

        Evento guardado = repo.save(evento);

        return mapper.aResponse(guardado);
    }
}
