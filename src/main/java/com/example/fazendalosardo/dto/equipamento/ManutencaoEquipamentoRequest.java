package com.example.fazendalosardo.dto.equipamento;

import com.example.fazendalosardo.model.enums.FormaPagamentoManutencao;
import com.example.fazendalosardo.model.enums.TipoManutencao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManutencaoEquipamentoRequest(
        @NotNull Long equipamentoId,
        @NotNull TipoManutencao tipo,
        String descricao,
        @NotNull LocalDate data,
        @NotNull @Positive BigDecimal valor,
        String observacao,
        @NotNull Long fazendaId,
        Long negocioId,
        Long safraId,
        Long projetoId,
        @NotNull FormaPagamentoManutencao formaPagamento,
        Long fornecedorId
) {}