package com.orion.eventos_service.Service;

import com.orion.eventos_service.DTO.EventoMapper;
import com.orion.eventos_service.DTO.EventoRequest;
import com.orion.eventos_service.DTO.EventoResponse;
import com.orion.eventos_service.Entity.Evento;
import com.orion.eventos_service.Repository.RepositoryEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {
    @Autowired
    private  RepositoryEvento repo;
    @Autowired
    private  EventoMapper mapper;
    @Transactional
    public EventoResponse guardar(EventoRequest dto, Long idCreador) {
        Evento evento = mapper.aEntidad(dto, idCreador);
        Evento guardado = repo.save(evento);
        return mapper.aResponse(guardado);
    }
    @Transactional(readOnly = true)
    public List<EventoResponse> obtenerTodos() {

        return repo.findAll()
                .stream()
                .map(mapper::aResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public EventoResponse actualizar(Long id, EventoRequest dto, Long idUsuario) {

        Evento evento = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (!evento.getIdCreador().equals(idUsuario)) {
            throw new RuntimeException("No tienes permiso para editar este evento");
        }

        evento.setNombre(dto.getNombre());
        evento.setLugar(dto.getLugar());
        evento.setFecha(dto.getFecha());

        Evento actualizado = repo.save(evento);
        return mapper.aResponse(actualizado);
    }
    @Transactional
    public void eliminar(Long id, Long idUsuario) {

        Evento evento = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));

        if (!evento.getIdCreador().equals(idUsuario)) {
            throw new RuntimeException("No tienes permiso para eliminar este evento");
        }

        repo.deleteById(id);
    }
    @Transactional(readOnly = true)
    public EventoResponse obtenerPorId(Long id) {
        Evento evento = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));
        return mapper.aResponse(evento);
    }

}
