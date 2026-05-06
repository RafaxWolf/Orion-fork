package com.orion.Usuarios.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    private String biografia;
    private String avatarUrl;
    private String ubicacion;

}
