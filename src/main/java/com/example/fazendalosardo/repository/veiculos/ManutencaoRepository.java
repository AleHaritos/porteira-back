package com.example.fazendalosardo.repository.veiculos;

import com.example.fazendalosardo.dto.veiculos.ManutencaoResumoResponse;
import com.example.fazendalosardo.model.Manutencao;
import com.example.fazendalosardo.model.enums.StatusParcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

    @Query("SELECT m FROM Manutencao m WHERE m.veiculo.id = :veiculoId AND m.ativo = true ORDER BY m.data DESC")
    Page<Manutencao> buscarPorVeiculo(@Param("veiculoId") Long veiculoId, Pageable pageable);

    @Query("SELECT m FROM Manutencao m WHERE m.fornecedor.id = :fornecedorId AND m.statusPagamento = :status AND m.ativo = true ORDER BY m.data DESC")
    List<Manutencao> listarPendentesPorFornecedor(@Param("fornecedorId") Long fornecedorId, @Param("status") StatusParcela status);

    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Manutencao m WHERE m.safra.id = :safraId AND m.ativo = true")
    BigDecimal somarValorPorSafra(@Param("safraId") Long safraId);

    @Query("""
            SELECT m FROM Manutencao m
            WHERE m.veiculo.id = :veiculoId
              AND m.ativo = true
              AND (CAST(:dataInicio AS date) IS NULL OR m.data >= :dataInicio)
              AND (CAST(:dataFim AS date) IS NULL OR m.data <= :dataFim)
            ORDER BY m.data DESC
            """)
    Page<Manutencao> buscarPorVeiculoEPeriodo(@Param("veiculoId") Long veiculoId,
                                              @Param("dataInicio") LocalDate dataInicio,
                                              @Param("dataFim") LocalDate dataFim,
                                              Pageable pageable);

    @Query("""
            SELECT new com.example.fazendalosardo.dto.veiculos.ManutencaoResumoResponse(
                COALESCE(SUM(m.valor), 0), COUNT(m))
            FROM Manutencao m
            WHERE m.veiculo.id = :veiculoId
              AND m.ativo = true
              AND (CAST(:dataInicio AS date) IS NULL OR m.data >= :dataInicio)
              AND (CAST(:dataFim AS date) IS NULL OR m.data <= :dataFim)
            """)
    ManutencaoResumoResponse buscarResumoPorVeiculo(@Param("veiculoId") Long veiculoId,
                                                    @Param("dataInicio") LocalDate dataInicio,
                                                    @Param("dataFim") LocalDate dataFim);

    @Query("""
        SELECT m FROM Manutencao m
        WHERE m.fazenda.id = :fazendaId
          AND m.ativo = true
          AND (:veiculoId IS NULL OR m.veiculo.id = :veiculoId)
          AND (CAST(:dataInicio AS date) IS NULL OR m.data >= :dataInicio)
          AND (CAST(:dataFim AS date) IS NULL OR m.data <= :dataFim)
        ORDER BY m.data DESC
        """)
    Page<Manutencao> buscarPorFazenda(@Param("fazendaId") Long fazendaId,
                                      @Param("veiculoId") Long veiculoId,
                                      @Param("dataInicio") LocalDate dataInicio,
                                      @Param("dataFim") LocalDate dataFim,
                                      Pageable pageable);
}