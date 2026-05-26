package com.orion.chatservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ConversacionDTO {


    private Long idChat;
    private String nombreUsuario;
    private String ultimoMensaje;
    private LocalDateTime fechaUltimoMensaje;

}
