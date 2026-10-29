package com.example.fazendalosardo.repository.estoque;

import com.example.fazendalosardo.model.estoque.CompraEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraEstoqueRepository extends JpaRepository<CompraEstoque, Long> {
    List<CompraEstoque> findByProdutoIdOrderByDataDescIdDesc(Long produtoId);
}