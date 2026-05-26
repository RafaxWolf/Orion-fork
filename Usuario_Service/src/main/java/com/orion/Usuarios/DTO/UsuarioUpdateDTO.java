package com.orion.Usuarios.DTO;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {

    private String username;
    private String email;
    private String password;
    private String avatarUrl;
    private String biografia;
    private String ubicacion;
}
