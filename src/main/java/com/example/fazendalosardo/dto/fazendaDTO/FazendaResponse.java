package com.example.fazendalosardo.dto.fazendaDTO;

import com.example.fazendalosardo.dto.UsuarioDTO;

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
