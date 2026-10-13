package com.example.fazendalosardo.repository.veiculos;

import com.example.fazendalosardo.model.Veiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("SELECT v FROM Veiculo v WHERE v.fazenda.id = :fazendaId AND v.ativo = true ORDER BY v.nome")
    Page<Veiculo> buscarPorFazenda(@Param("fazendaId") Long fazendaId, Pageable pageable);

    boolean existsByPlacaIgnoreCase(String placa);
}