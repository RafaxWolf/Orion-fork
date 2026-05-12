package com.orion.Usuarios.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {

    String username;
    String avatarUrl;
    String biografia;
    String ubicacion;

}

