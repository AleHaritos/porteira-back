package com.example.fazendalosardo.repository;

import com.example.fazendalosardo.dto.financeiro.ResumoFinanceiroResponse;
import com.example.fazendalosardo.model.Transacao;
import com.example.fazendalosardo.model.enums.TipoTransacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    @Query("""
            SELECT t FROM Transacao t
            WHERE t.fazenda.id = :fazendaId
              AND t.ativo = true
              AND t.data >= COALESCE(:dataInicio, t.data)
              AND t.data <= COALESCE(:dataFim, t.data)
              AND t.tipo = COALESCE(:tipo, t.tipo)
            ORDER BY t.data DESC
            """)
    Page<Transacao> buscarComFiltros(@Param("fazendaId") Long fazendaId,
                                     @Param("dataInicio") LocalDate dataInicio,
                                     @Param("dataFim") LocalDate dataFim,
                                     @Param("tipo") TipoTransacao tipo,
                                     Pageable pageable);

    @Query("""
        SELECT new com.example.fazendalosardo.dto.financeiro.ResumoFinanceiroResponse(
            COALESCE(SUM(CASE WHEN t.tipo = 'RECEITA' THEN t.valor ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.tipo = 'GASTO' THEN t.valor ELSE 0 END), 0)
        )
        FROM Transacao t
        WHERE t.fazenda.id = :fazendaId
          AND t.ativo = true
          AND t.data >= COALESCE(:dataInicio, t.data)
          AND t.data <= COALESCE(:dataFim, t.data)
        """)
    ResumoFinanceiroResponse buscarResumo(@Param("fazendaId") Long fazendaId,
                                          @Param("dataInicio") LocalDate dataInicio,
                                          @Param("dataFim") LocalDate dataFim);

    Page<Transacao> findByNegocioIdAndAtivoTrue(Long negocioId, Pageable pageable);

    Page<Transacao> findBySafraIdAndAtivoTrue(Long safraId, Pageable pageable);
}
