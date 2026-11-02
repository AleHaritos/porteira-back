package com.example.fazendalosardo.repository;

import com.example.fazendalosardo.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {

    @Query("""
            SELECT n FROM Nota n
            JOIN FETCH n.fazenda
            LEFT JOIN FETCH n.negocio
            LEFT JOIN FETCH n.safra
            LEFT JOIN FETCH n.projeto
            WHERE n.fazenda.id = :fazendaId
            ORDER BY n.atualizadoEm DESC
            """)
    List<Nota> buscarPorFazenda(@Param("fazendaId") Long fazendaId);
}