package com.example.fazendalosardo.dto;

import java.math.BigDecimal;
import java.util.List;

public record FazendaResponse(
        Long id,
        String nome,
        BigDecimal hectares,
        String localizacao,
        UsuarioDTO dono,
        List<UsuarioDTO> colaboradores
) {
}
