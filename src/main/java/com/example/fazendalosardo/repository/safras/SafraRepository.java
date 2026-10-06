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
        AND (:dataInicioDe IS NULL OR s.dataInicio >= :dataInicioDe)
        AND (:dataInicioAte IS NULL OR s.dataInicio <= :dataInicioAte)
        """)
    Page<Safra> buscarPorFazenda(
            @Param("fazendaId") Long fazendaId,
            @Param("statusExcluido") StatusSafra statusExcluido,
            @Param("dataInicioDe") LocalDate dataInicioDe,
            @Param("dataInicioAte") LocalDate dataInicioAte,
            Pageable pageable
    );
}
