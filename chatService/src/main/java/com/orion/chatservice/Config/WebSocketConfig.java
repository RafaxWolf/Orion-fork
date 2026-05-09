package com.orion.chatservice.Config;


import com.orion.chatservice.Security.JwtChannelInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtChannelInterceptor jwtChannelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // para que react abra la llamada tendra que llegar hasta este
        // endpoint:

        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // prefijo para los mensajes que van desde este backend
        // hasta react (para notificaiones de nuevos mensajes por ej)
        registry.enableSimpleBroker("/topic","/queue");


        // Prefijo para los mensajes que react envia hacia el server (cuando escribes un mensaje)
        registry.setApplicationDestinationPrefixes("/app");

        // etiqueta para identificar a que usuario especifico
        // mandarle mensajes.
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(jwtChannelInterceptor);
    }

}
