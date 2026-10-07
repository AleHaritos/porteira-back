package com.example.fazendalosardo.dto.fazendaDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FazendaUpdateRequest(
        @NotBlank String nome,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal hectares,
        String localizacao
) {
}
