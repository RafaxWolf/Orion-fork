package com.orion.eventos_service.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class EventoResponse {
    private Long idEvento;
    private Long idCreador;
    private String nombre;
    private String lugar;
    private LocalDateTime fecha;
    private LocalDateTime creadoEl;
    private List<Long> asistentes;
    private int cantidadAsistentes;
}
