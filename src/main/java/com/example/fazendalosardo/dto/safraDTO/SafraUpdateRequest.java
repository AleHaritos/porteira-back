package com.example.fazendalosardo.dto.safraDTO;

import com.example.fazendalosardo.model.enums.StatusSafra;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record SafraUpdateRequest(
        @NotBlank String nome,
        String cultura,
        @NotBlank @Size(max = 9) String anoAgricola,
        @NotNull @DecimalMin(value = "0.01") BigDecimal areaTotal,
        @NotNull StatusSafra status,
        String observacoes
) {
}
