package com.example.fazendalosardo.repository.estoque;

import com.example.fazendalosardo.model.estoque.ProdutoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoEstoqueRepository extends JpaRepository<ProdutoEstoque, Long> {
    List<ProdutoEstoque> findByFazendaIdOrderByNomeAsc(Long fazendaId);
}