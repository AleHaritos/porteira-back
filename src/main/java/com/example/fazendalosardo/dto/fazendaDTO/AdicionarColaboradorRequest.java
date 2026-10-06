package com.example.fazendalosardo.dto.fazendaDTO;

import jakarta.validation.constraints.NotBlank;

public record AdicionarColaboradorRequest(@NotBlank  String numero) {
}
