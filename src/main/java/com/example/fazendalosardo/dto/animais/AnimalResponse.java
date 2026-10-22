package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.TipoAnimal;

public record AnimalResponse(
        Long id,
        TipoAnimal tipo,
        Long loteId,
        String loteNome,
        Long piqueteId,
        String piqueteNome,
        String descricao
) {
}