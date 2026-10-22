package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.TipoAnimal;
import jakarta.validation.constraints.NotNull;

public record AnimalRequest(
        @NotNull TipoAnimal tipo,
        String descricao,
        Long loteId,
        @NotNull Long piqueteId,
        @NotNull Long fazendaId
) {
}