package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.OperacaoVenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OperacaoVendaRepository extends JpaRepository<OperacaoVenda, Long> {

    @Query("""
            SELECT o FROM OperacaoVenda o
            LEFT JOIN FETCH o.itens i
            LEFT JOIN FETCH i.animal
            WHERE o.fazenda.id = :fazendaId
            ORDER BY o.data DESC, o.id DESC
            """)
    List<OperacaoVenda> buscarPorFazenda(@Param("fazendaId") Long fazendaId);

    @Query("""
            SELECT COALESCE(SUM(o.valorVenda), 0) as receitaTotal,
                   COALESCE(SUM(o.custoTotal), 0) as custoTotal,
                   COALESCE(SUM(o.lucro), 0) as lucroTotal
            FROM OperacaoVenda o
            WHERE o.fazenda.id = :fazendaId
            AND o.data BETWEEN :dataInicio AND :dataFim
            """)
    ResumoVendaProjection somarPorPeriodo(@Param("fazendaId") Long fazendaId,
                                          @Param("dataInicio") LocalDate dataInicio,
                                          @Param("dataFim") LocalDate dataFim);

    @Query("""
            SELECT o FROM OperacaoVenda o
            LEFT JOIN FETCH o.itens i
            LEFT JOIN FETCH i.animal
            WHERE o.fazenda.id = :fazendaId
            AND o.data BETWEEN :dataInicio AND :dataFim
            ORDER BY o.data DESC, o.id DESC
            """)
    List<OperacaoVenda> buscarPorFazendaEPeriodo(@Param("fazendaId") Long fazendaId,
                                                 @Param("dataInicio") LocalDate dataInicio,
                                                 @Param("dataFim") LocalDate dataFim);

    @Query("""
            SELECT o FROM OperacaoVenda o
            LEFT JOIN FETCH o.itens i
            LEFT JOIN FETCH i.animal
            WHERE o.fazenda.id = :fazendaId
            AND o.data BETWEEN :dataInicio AND :dataFim
            ORDER BY o.data DESC, o.id DESC
            """)
    Page<OperacaoVenda> buscarPorFazendaEPeriodo(@Param("fazendaId") Long fazendaId,
                                                 @Param("dataInicio") LocalDate dataInicio,
                                                 @Param("dataFim") LocalDate dataFim,
                                                 Pageable pageable);
}