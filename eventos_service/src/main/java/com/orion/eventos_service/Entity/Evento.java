package com.orion.eventos_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "Evento")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvento;
    @Column(nullable = false)
    private Long idCreador;
    @Column(nullable = false, length = 200)
    private String nombre;
    @Column(nullable = false, length = 200)
    private String lugar;
    @ElementCollection
    @CollectionTable(name = "evento_asistentes", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "usuario_id")
    private List<Long> asistentes;

    @Column(nullable = false)

    private LocalDateTime fecha;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEl;
}
