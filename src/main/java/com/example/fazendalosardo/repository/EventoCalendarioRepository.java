package com.example.fazendalosardo.repository;

import com.example.fazendalosardo.model.EventoCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventoCalendarioRepository extends JpaRepository<EventoCalendario, Long> {

    @Query("""
            SELECT e FROM EventoCalendario e
            WHERE e.fazenda.id = :fazendaId
              AND e.data BETWEEN :dataInicio AND :dataFim
            ORDER BY e.data ASC, e.horaInicio ASC
            """)
    List<EventoCalendario> buscarPorFazendaEPeriodo(@Param("fazendaId") Long fazendaId,
                                                    @Param("dataInicio") LocalDate dataInicio,
                                                    @Param("dataFim") LocalDate dataFim);
}