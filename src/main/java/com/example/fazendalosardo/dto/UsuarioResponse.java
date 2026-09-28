package com.example.fazendalosardo.dto;

public record UsuarioResponse(
        Long id,
        String nome,
        String numero,
        Boolean admin
) {
}
