package com.example.fazendalosardo.repository.veiculos;

import com.example.fazendalosardo.model.Manutencao;
import com.example.fazendalosardo.model.enums.StatusParcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

    @Query("SELECT m FROM Manutencao m WHERE m.veiculo.id = :veiculoId AND m.ativo = true ORDER BY m.data DESC")
    Page<Manutencao> buscarPorVeiculo(@Param("veiculoId") Long veiculoId, Pageable pageable);

    @Query("SELECT m FROM Manutencao m WHERE m.fornecedor.id = :fornecedorId AND m.statusPagamento = :status ORDER BY m.data DESC")
    List<Manutencao> listarPendentesPorFornecedor(@Param("fornecedorId") Long fornecedorId, @Param("status") StatusParcela status);
}