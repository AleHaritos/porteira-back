package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.CuidadoAnimal;
import com.example.fazendalosardo.model.enums.TipoCuidado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CuidadoAnimalRepository extends JpaRepository<CuidadoAnimal, Long> {

    @Query("""
        SELECT c FROM CuidadoAnimal c
        LEFT JOIN FETCH c.animal
        LEFT JOIN FETCH c.lote
        LEFT JOIN FETCH c.negocio
        LEFT JOIN FETCH c.safra
        WHERE c.piquete.id = :piqueteId
        AND c.tipo = :tipo
        ORDER BY c.data DESC, c.id DESC
        """)
    List<CuidadoAnimal> buscarPorPiqueteETipo(@Param("piqueteId") Long piqueteId, @Param("tipo") TipoCuidado tipo);
}