package com.example.fazendalosardo.repository;

import com.example.fazendalosardo.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query("SELECT t FROM Tarefa t WHERE t.fazenda.id = :fazendaId ORDER BY t.status ASC, t.ordem ASC")
    List<Tarefa> buscarPorFazenda(@Param("fazendaId") Long fazendaId);
}