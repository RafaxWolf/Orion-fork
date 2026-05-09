package com.orion.chatservice.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId; // quien va a enviar el mensaje

    @Column(nullable = false)
    private Long receiverId; // a quien le llega el mensaje

    @Column
    private String nombreEmisor;

    @Column(nullable = false, length = 1000)
    private String contenido;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaEnvio;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }


}
