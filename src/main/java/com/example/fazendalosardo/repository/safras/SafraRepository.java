package com.example.fazendalosardo.repository.safras;

import com.example.fazendalosardo.model.enums.StatusSafra;
import com.example.fazendalosardo.model.safras.Safra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SafraRepository extends JpaRepository<Safra, Long> {

    @Query("""
        SELECT s FROM Safra s
        WHERE s.fazenda.id = :fazendaId
        AND s.status <> :statusExcluido
        AND (:anoAgricola IS NULL OR s.anoAgricola LIKE %:anoAgricola%)
        """)
    Page<Safra> buscarPorFazenda(
            @Param("fazendaId") Long fazendaId,
            @Param("statusExcluido") StatusSafra statusExcluido,
            @Param("anoAgricola") String anoAgricola,
            Pageable pageable
    );
}
