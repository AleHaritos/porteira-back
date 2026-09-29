package com.example.fazendalosardo.dto;

import java.math.BigDecimal;

public record FazendaResumoResponse(
        Long id,
        String nome,
        BigDecimal hectares,
        String localizacao,
        UsuarioDTO dono
) {
}
