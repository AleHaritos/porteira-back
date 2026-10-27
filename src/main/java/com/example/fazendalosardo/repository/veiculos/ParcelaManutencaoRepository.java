package com.example.fazendalosardo.repository.veiculos;

import com.example.fazendalosardo.model.ParcelaManutencao;
import com.example.fazendalosardo.model.enums.StatusParcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParcelaManutencaoRepository extends JpaRepository<ParcelaManutencao, Long> {

    @Query("""
            SELECT p FROM ParcelaManutencao p
            WHERE p.manutencaoParcelada.fazenda.id = :fazendaId
              AND p.manutencaoParcelada.ativo = true
              AND p.status = COALESCE(:status, p.status)
            ORDER BY p.dataVencimento ASC
            """)
    Page<ParcelaManutencao> buscarPorFazenda(@Param("fazendaId") Long fazendaId,
                                             @Param("status") StatusParcela status,
                                             Pageable pageable);
}