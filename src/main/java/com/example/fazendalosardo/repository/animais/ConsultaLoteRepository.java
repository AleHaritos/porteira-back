package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.ConsultaLote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsultaLoteRepository extends JpaRepository<ConsultaLote, Long> {

    @Query("""
        SELECT c FROM ConsultaLote c
        LEFT JOIN FETCH c.negocio
        LEFT JOIN FETCH c.safra
        WHERE c.lote.id = :loteId
        ORDER BY c.data DESC
        """)
    Page<ConsultaLote> buscarPorLote(@Param("loteId") Long loteId, Pageable pageable);
}