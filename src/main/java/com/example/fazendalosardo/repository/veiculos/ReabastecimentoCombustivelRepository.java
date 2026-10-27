package com.example.fazendalosardo.repository.veiculos;

import com.example.fazendalosardo.model.ReabastecimentoCombustivel;
import com.example.fazendalosardo.model.enums.TipoCombustivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReabastecimentoCombustivelRepository extends JpaRepository<ReabastecimentoCombustivel, Long> {

    @Query("""
            SELECT r FROM ReabastecimentoCombustivel r
            WHERE r.fazenda.id = :fazendaId
              AND r.tipoCombustivel = :tipoCombustivel
              AND (:dataInicio IS NULL OR r.data >= :dataInicio)
              AND (:dataFim IS NULL OR r.data <= :dataFim)
            ORDER BY r.data DESC, r.id DESC
            """)
    List<ReabastecimentoCombustivel> buscarHistorico(Long fazendaId, TipoCombustivel tipoCombustivel,
                                                     LocalDate dataInicio, LocalDate dataFim);
}