package com.posteos.DTO;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//De BDD a afuera //
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PostResponseDTO {
    private Long id;
    private Long userId;
    private String content;
    private String mediaUrl;
    private LocalDateTime creado_el;

}
