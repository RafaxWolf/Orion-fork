package com.orion.chatservice.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private static final String SECRET_KEY = "0mfQfctbNBt7Tb4Ej8aXQQebUc8zmnhpkZKObqzxUCi";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);

        // solo se intercepta en el primer mensaje, cuando el user
        // se intenta conectar
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");


            // se extrae el token del header de stomp que se envia desde react
            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);

                try {
                    // valida el token
                    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
                    SecretKey key =  Keys.hmacShaKeyFor(keyBytes);
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    String username = claims.getSubject();
                    Object idObj = claims.get("id");
                    Long userId = idObj != null ? Long.valueOf(idObj.toString()) : null;

                    // se le dice a webscket quien es el dueño de la conexion
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId,null,null);

                    accessor.setUser(authentication);

                    System.out.println("Usuario "+ username + " conectado al Chat");

                    if (accessor.getSessionAttributes() != null){
                        accessor.getSessionAttributes().put("realUserId",userId);
                        accessor.getSessionAttributes().put("realUsername",username);
                    }

                } catch (Exception e) {
                    System.out.println("Token invalido en WebSocket: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Acceso denegado al chat");
                }

            } else {
                System.out.println("Intento de conexion al chat sin token");
                throw new RuntimeException("No se encontro el token JWT");
            }

        }

        return message;
    }






}
