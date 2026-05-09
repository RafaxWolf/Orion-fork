package com.orion.chatservice.Controller;


import com.orion.chatservice.Entity.Mensaje;
import com.orion.chatservice.Repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MensajeRepository repo;


    // react enviara mensajes a la ruta: /app/chat.enviar
    @MessageMapping("/chat.enviar")
    public void procesarMensaje(@Payload Mensaje mensaje) {

        Mensaje mensajeGuardado = repo.save(mensaje);

        // Se le envia en vivo el mensaje al receptor
        // SI el reciverId es '5' esto lo envia al canal especial con el usuario
        messagingTemplate.convertAndSendToUser(
                String.valueOf(mensaje.getReceiverId()),
                "/queue/mensajes",
                mensaje
        );

        System.out.println("Mensaje enviado a: " + mensaje.getReceiverId());

    }



    @MessageMapping("/chat.global")
    public void procesarGlobalMensaje(@Payload Mensaje mensaje) {


        // se le asigna 0 o null al receiver para indicar que es para todo el mundo
        mensaje.setReceiverId(0L);
        Mensaje mensajeGuardado = repo.save(mensaje);

        messagingTemplate.convertAndSend("/topic/publico",mensajeGuardado);

        System.out.println("Mensaje global enviado por " + mensaje.getSenderId());



    }



}
