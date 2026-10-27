package com.example.fazendalosardo.repository.equipamento;

import com.example.fazendalosardo.model.ManutencaoEquipamento;
import com.example.fazendalosardo.model.enums.StatusParcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ManutencaoEquipamentoRepository extends JpaRepository<ManutencaoEquipamento, Long> {

    @Query("SELECT m FROM ManutencaoEquipamento m WHERE m.equipamento.id = :equipamentoId AND m.ativo = true ORDER BY m.data DESC")
    Page<ManutencaoEquipamento> buscarPorEquipamento(@Param("equipamentoId") Long equipamentoId, Pageable pageable);

    @Query("SELECT m FROM ManutencaoEquipamento m WHERE m.fornecedor.id = :fornecedorId AND m.statusPagamento = :status AND m.ativo = true ORDER BY m.data DESC")
    List<ManutencaoEquipamento> listarPendentesPorFornecedor(@Param("fornecedorId") Long fornecedorId, @Param("status") StatusParcela status);

    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM ManutencaoEquipamento m WHERE m.safra.id = :safraId AND m.ativo = true")
    BigDecimal somarValorPorSafra(@Param("safraId") Long safraId);

    @Query("""
        SELECT m FROM ManutencaoEquipamento m
        WHERE m.fazenda.id = :fazendaId
          AND m.ativo = true
          AND (:equipamentoId IS NULL OR m.equipamento.id = :equipamentoId)
          AND (CAST(:dataInicio AS date) IS NULL OR m.data >= :dataInicio)
          AND (CAST(:dataFim AS date) IS NULL OR m.data <= :dataFim)
        ORDER BY m.data DESC
        """)
    Page<ManutencaoEquipamento> buscarPorFazenda(@Param("fazendaId") Long fazendaId,
                                                 @Param("equipamentoId") Long equipamentoId,
                                                 @Param("dataInicio") LocalDate dataInicio,
                                                 @Param("dataFim") LocalDate dataFim,
                                                 Pageable pageable);
}