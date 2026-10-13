package com.example.fazendalosardo.repository.veiculos;

import com.example.fazendalosardo.model.Abastecimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AbastecimentoRepository extends JpaRepository<Abastecimento, Long> {

    @Query("SELECT a FROM Abastecimento a WHERE a.veiculo.id = :veiculoId AND a.ativo = true ORDER BY a.data DESC")
    Page<Abastecimento> buscarPorVeiculo(@Param("veiculoId") Long veiculoId, Pageable pageable);
}