package com.example.fazendalosardo.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String numero,
                           @NotBlank String senha) {
}
