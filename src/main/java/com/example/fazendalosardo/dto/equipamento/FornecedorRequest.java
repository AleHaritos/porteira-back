package com.example.fazendalosardo.dto.equipamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FornecedorRequest(
        @NotBlank String nome,
        String especialidade,
        String observacao,
        @NotNull Long fazendaId
) {
}
