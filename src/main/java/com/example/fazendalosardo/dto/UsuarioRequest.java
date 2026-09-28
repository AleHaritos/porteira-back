package com.example.fazendalosardo.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequest(@NotBlank(message = "Nome é obrigatório")
                             String nome,
                             @NotBlank(message = "Núemero é obrigatório")
                             String numero) {
}
