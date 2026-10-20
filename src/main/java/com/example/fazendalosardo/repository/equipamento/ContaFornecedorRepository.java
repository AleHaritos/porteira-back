package com.example.fazendalosardo.repository.equipamento;

import com.example.fazendalosardo.model.ContaFornecedor;
import com.example.fazendalosardo.model.enums.StatusParcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContaFornecedorRepository extends JpaRepository<ContaFornecedor, Long> {

    @Query("SELECT c FROM ContaFornecedor c WHERE c.fornecedor.id = :fornecedorId AND c.statusPagamento = :status ORDER BY c.data DESC")
    List<ContaFornecedor> listarPendentesPorFornecedor(@Param("fornecedorId") Long fornecedorId, @Param("status") StatusParcela status);
}