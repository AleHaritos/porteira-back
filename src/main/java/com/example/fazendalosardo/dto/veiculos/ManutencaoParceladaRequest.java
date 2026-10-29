package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.enums.TipoManutencao;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ManutencaoParceladaRequest(
        @NotNull Long fazendaId,
        Long veiculoId,
        Long equipamentoId,
        @NotNull TipoManutencao tipo,
        String descricao,
        Integer kmAtual,
        String observacao,
        @NotNull Long fornecedorId,
        Long negocioId,
        Long safraId,
        Long projetoId,
        @NotNull LocalDate primeiraDataVencimento,
        @NotEmpty List<@Positive BigDecimal> valoresParcelas
) {
}