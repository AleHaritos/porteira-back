package com.example.fazendalosardo.dto.animais;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TransferenciaAnimalRequest(
        @NotEmpty List<Long> animalIds,
        @NotNull Long piqueteDestinoId,
        @NotNull Long fazendaId
) {}