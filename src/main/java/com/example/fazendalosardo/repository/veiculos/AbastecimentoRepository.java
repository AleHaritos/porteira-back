package com.example.fazendalosardo.repository.veiculos;

import com.example.fazendalosardo.dto.veiculos.AbastecimentoResumoResponse;
import com.example.fazendalosardo.model.Abastecimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AbastecimentoRepository extends JpaRepository<Abastecimento, Long> {

    @Query("SELECT a FROM Abastecimento a WHERE a.veiculo.id = :veiculoId AND a.ativo = true ORDER BY a.data DESC")
    Page<Abastecimento> buscarPorVeiculo(@Param("veiculoId") Long veiculoId, Pageable pageable);

    @Query("SELECT a FROM Abastecimento a WHERE a.posto.id = :postoId AND a.ativo = true ORDER BY a.data DESC")
    Page<Abastecimento> buscarPorPosto(@Param("postoId") Long postoId, Pageable pageable);

    @Query("""
            SELECT a FROM Abastecimento a
            WHERE a.fazenda.id = :fazendaId
              AND a.ativo = true
              AND (:veiculoId IS NULL OR a.veiculo.id = :veiculoId)
              AND (CAST(:dataInicio AS date) IS NULL OR a.data >= :dataInicio)
              AND (CAST(:dataFim AS date) IS NULL OR a.data <= :dataFim)
              AND (:negocioId IS NULL OR a.negocio.id = :negocioId)
              AND (:safraId IS NULL OR a.safra.id = :safraId)
            ORDER BY a.data DESC
            """)
    Page<Abastecimento> buscarPorFazenda(@Param("fazendaId") Long fazendaId,
                                         @Param("veiculoId") Long veiculoId,
                                         @Param("dataInicio") LocalDate dataInicio,
                                         @Param("dataFim") LocalDate dataFim,
                                         @Param("negocioId") Long negocioId,
                                         @Param("safraId") Long safraId,
                                         Pageable pageable);

    @Query("""
            SELECT new com.example.fazendalosardo.dto.veiculos.AbastecimentoResumoResponse(
                COALESCE(SUM(a.valorTotal), 0), COALESCE(SUM(a.litros), 0), COUNT(a))
            FROM Abastecimento a
            WHERE a.fazenda.id = :fazendaId
              AND a.ativo = true
              AND (:veiculoId IS NULL OR a.veiculo.id = :veiculoId)
              AND (CAST(:dataInicio AS date) IS NULL OR a.data >= :dataInicio)
              AND (CAST(:dataFim AS date) IS NULL OR a.data <= :dataFim)
              AND (:negocioId IS NULL OR a.negocio.id = :negocioId)
              AND (:safraId IS NULL OR a.safra.id = :safraId)
            """)
    AbastecimentoResumoResponse buscarResumo(@Param("fazendaId") Long fazendaId,
                                             @Param("veiculoId") Long veiculoId,
                                             @Param("dataInicio") LocalDate dataInicio,
                                             @Param("dataFim") LocalDate dataFim,
                                             @Param("negocioId") Long negocioId,
                                             @Param("safraId") Long safraId);

    @Query("SELECT COALESCE(SUM(a.valorTotal), 0) FROM Abastecimento a WHERE a.safra.id = :safraId AND a.ativo = true")
    BigDecimal somarValorPorSafra(@Param("safraId") Long safraId);
}