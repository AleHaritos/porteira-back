package com.example.fazendalosardo.repository.equipamento;

import com.example.fazendalosardo.model.ManutencaoEquipamento;
import com.example.fazendalosardo.model.enums.StatusParcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManutencaoEquipamentoRepository extends JpaRepository<ManutencaoEquipamento, Long> {

    @Query("SELECT m FROM ManutencaoEquipamento m WHERE m.equipamento.id = :equipamentoId AND m.ativo = true ORDER BY m.data DESC")
    Page<ManutencaoEquipamento> buscarPorEquipamento(@Param("equipamentoId") Long equipamentoId, Pageable pageable);

    @Query("SELECT m FROM ManutencaoEquipamento m WHERE m.fornecedor.id = :fornecedorId AND m.statusPagamento = :status ORDER BY m.data DESC")
    List<ManutencaoEquipamento> listarPendentesPorFornecedor(@Param("fornecedorId") Long fornecedorId, @Param("status") StatusParcela status);
}