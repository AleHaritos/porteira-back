package com.example.fazendalosardo.dto.fazendaDTO;

import com.example.fazendalosardo.dto.UsuarioDTO;

import java.math.BigDecimal;

public record FazendaResumoResponse(
        Long id,
        String nome,
        BigDecimal hectares,
        String localizacao,
        UsuarioDTO dono
) {
}
