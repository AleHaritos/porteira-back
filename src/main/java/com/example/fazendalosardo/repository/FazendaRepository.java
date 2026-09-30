package com.example.fazendalosardo.repository;

import com.example.fazendalosardo.model.Fazenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FazendaRepository extends JpaRepository<Fazenda, Long> {

    @Query("""
    SELECT f FROM Fazenda f
    WHERE f.id IN (
        SELECT fc.fazenda.id FROM FazendaColaborador fc WHERE fc.colaborador.id = :usuarioId
    )
""")
    Page<Fazenda> buscarPorColaborador(@Param("usuarioId") Long usuarioId, Pageable pageable);
}
