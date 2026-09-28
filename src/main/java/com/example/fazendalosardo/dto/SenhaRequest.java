package com.example.fazendalosardo.dto;

import jakarta.validation.constraints.NotBlank;

public record SenhaRequest(@NotBlank String numero, @NotBlank String senha) {
}
