package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.CuidadoAnimal;
import com.example.fazendalosardo.model.enums.TipoCuidado;
import com.example.fazendalosardo.model.enums.TipoVinculoCuidado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CuidadoAnimalRepository extends JpaRepository<CuidadoAnimal, Long> {

    @Query("""
            SELECT c FROM CuidadoAnimal c
            LEFT JOIN FETCH c.animal
            LEFT JOIN FETCH c.lote
            LEFT JOIN FETCH c.racao
            WHERE c.piquete.id = :piqueteId
            AND c.tipo = :tipo
            ORDER BY c.data DESC, c.id DESC
            """)
    List<CuidadoAnimal> buscarPorPiqueteETipo(@Param("piqueteId") Long piqueteId, @Param("tipo") TipoCuidado tipo);

    List<CuidadoAnimal> findByAnimalIdAndVinculoTipo(Long animalId, TipoVinculoCuidado vinculoTipo);

    List<CuidadoAnimal> findByLoteIdAndVinculoTipo(Long loteId, TipoVinculoCuidado vinculoTipo);

    List<CuidadoAnimal> findByPiqueteIdAndVinculoTipo(Long piqueteId, TipoVinculoCuidado vinculoTipo);

    @Query("""
        SELECT c FROM CuidadoAnimal c
        WHERE c.piquete.id = :piqueteId
        AND c.tipo = :tipo
        AND c.data BETWEEN :dataInicio AND :dataFim
        ORDER BY c.data DESC, c.id DESC
        """)
    Page<CuidadoAnimal> buscarPorPiqueteTipoEPeriodo(@Param("piqueteId") Long piqueteId,
                                                     @Param("tipo") TipoCuidado tipo,
                                                     @Param("dataInicio") LocalDate dataInicio,
                                                     @Param("dataFim") LocalDate dataFim,
                                                     Pageable pageable);

    @Query("""
            SELECT c FROM CuidadoAnimal c
            LEFT JOIN FETCH c.piquete
            LEFT JOIN FETCH c.lote
            LEFT JOIN FETCH c.animal
            WHERE c.fazenda.id = :fazendaId
            AND c.data BETWEEN :dataInicio AND :dataFim
            ORDER BY c.data DESC, c.id DESC
            """)
    List<CuidadoAnimal> buscarPorFazendaEPeriodo(@Param("fazendaId") Long fazendaId,
                                                 @Param("dataInicio") LocalDate dataInicio,
                                                 @Param("dataFim") LocalDate dataFim);

}