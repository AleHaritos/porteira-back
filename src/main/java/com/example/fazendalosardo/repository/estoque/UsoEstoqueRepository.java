package com.example.fazendalosardo.repository.estoque;

import com.example.fazendalosardo.model.estoque.UsoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsoEstoqueRepository extends JpaRepository<UsoEstoque, Long> {
    List<UsoEstoque> findByProdutoIdOrderByDataDescIdDesc(Long produtoId);
}