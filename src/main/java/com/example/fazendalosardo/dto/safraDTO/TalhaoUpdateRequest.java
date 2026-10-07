package com.example.fazendalosardo.dto.safraDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TalhaoUpdateRequest(
        @NotBlank String nome,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal areaHectares,
        String localizacao,
        String observacao
) {
}
