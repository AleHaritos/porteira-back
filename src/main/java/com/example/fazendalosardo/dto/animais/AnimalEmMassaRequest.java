package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.SexoAnimal;
import com.example.fazendalosardo.model.enums.TipoAnimal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AnimalEmMassaRequest(
        @NotNull Long fazendaId,
        @NotNull Long piqueteId,
        @NotEmpty @Valid List<AnimalItem> animais
) {
    public record AnimalItem(
            @NotNull TipoAnimal tipo,
            Long loteId,
            String descricao,
            SexoAnimal sexo,
            BigDecimal peso,
            LocalDate dataNascimento,
            String raca,
            BigDecimal valorCompra
    ) {
    }
}