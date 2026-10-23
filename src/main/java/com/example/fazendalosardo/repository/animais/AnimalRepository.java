package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.Animal;
import com.example.fazendalosardo.model.enums.TipoAnimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    long countByLoteIdAndAtivoTrue(Long loteId);

    long countByPiqueteIdAndAtivoTrue(Long piqueteId);

    @Query("""
        SELECT a FROM Animal a
        LEFT JOIN FETCH a.lote
        LEFT JOIN FETCH a.piquete
        WHERE a.fazenda.id = :fazendaId
        AND a.ativo = true
        AND (:piqueteId IS NULL OR a.piquete.id = :piqueteId)
        AND (:loteId IS NULL OR a.lote.id = :loteId)
        AND (:tipo IS NULL OR a.tipo = :tipo)
        ORDER BY a.id DESC
        """)
    Page<Animal> buscarComFiltros(@Param("fazendaId") Long fazendaId, @Param("piqueteId") Long piqueteId,
                                  @Param("loteId") Long loteId, @Param("tipo") TipoAnimal tipo, Pageable pageable);

    List<Animal> findByPiqueteIdAndAtivoTrue(Long piqueteId);

    List<Animal> findByLoteIdAndPiqueteIdAndAtivoTrue(Long loteId, Long piqueteId);

    long countByFazendaId(Long fazendaId);

    boolean existsByFazendaIdAndBrinco(Long fazendaId, String brinco);

    boolean existsByFazendaIdAndBrincoAndIdNot(Long fazendaId, String brinco, Long id);
}