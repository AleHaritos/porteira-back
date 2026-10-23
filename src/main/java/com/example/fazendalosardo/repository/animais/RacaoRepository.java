package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.Racao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RacaoRepository extends JpaRepository<Racao, Long> {
    @Query("""
        SELECT r FROM Racao r
        LEFT JOIN FETCH r.ingredientes ri
        LEFT JOIN FETCH ri.ingrediente
        WHERE r.fazenda.id = :fazendaId AND r.ativo = true
        ORDER BY r.nome
        """)
    List<Racao> buscarAtivasPorFazenda(@Param("fazendaId") Long fazendaId);
}