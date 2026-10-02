package com.example.fazendalosardo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequest(@NotBlank(message = "Nome é obrigatório")
                             String nome,
                             @NotBlank(message = "Número é obrigatório")
                             String numero,
                             @NotNull
                             Boolean admin) {
}
