package com.example.fazendalosardo.repository.financeiro;

import com.example.fazendalosardo.model.ParcelaContaPendente;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.model.enums.TipoTransacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParcelaContaPendenteRepository extends JpaRepository<ParcelaContaPendente, Long> {

    @Query("""
            SELECT p FROM ParcelaContaPendente p
            WHERE p.contaPendente.fazenda.id = :fazendaId
              AND p.contaPendente.ativo = true
              AND p.status = COALESCE(:status, p.status)
              AND p.contaPendente.tipo = COALESCE(:tipo, p.contaPendente.tipo)
            ORDER BY p.dataVencimento ASC
            """)
    Page<ParcelaContaPendente> buscarPorFazenda(@Param("fazendaId") Long fazendaId,
                                                @Param("status") StatusParcela status,
                                                @Param("tipo") TipoTransacao tipo,
                                                Pageable pageable);
}