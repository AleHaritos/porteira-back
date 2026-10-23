package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.CuidadoAnimalRateio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CuidadoAnimalRateioRepository extends JpaRepository<CuidadoAnimalRateio, Long> {

    @Query("""
        SELECT r FROM CuidadoAnimalRateio r
        JOIN FETCH r.cuidadoAnimal c
        LEFT JOIN FETCH c.piquete
        LEFT JOIN FETCH c.lote
        WHERE r.animal.id = :animalId
        ORDER BY c.data DESC, c.id DESC
        """)
    List<CuidadoAnimalRateio> buscarPorAnimal(@Param("animalId") Long animalId);

    @Query("""
        SELECT r.cuidadoAnimal.tipo as tipo, COALESCE(SUM(r.custoAtribuido), 0) as total
        FROM CuidadoAnimalRateio r
        WHERE r.cuidadoAnimal.fazenda.id = :fazendaId
        AND r.cuidadoAnimal.data BETWEEN :dataInicio AND :dataFim
        GROUP BY r.cuidadoAnimal.tipo
        """)
    List<CustoPorTipoProjection> somarCustoPorTipo(@Param("fazendaId") Long fazendaId,
                                                   @Param("dataInicio") LocalDate dataInicio,
                                                   @Param("dataFim") LocalDate dataFim);

    void deleteByCuidadoAnimalId(Long cuidadoAnimalId);
}