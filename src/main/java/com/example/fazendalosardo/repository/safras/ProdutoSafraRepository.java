package com.example.fazendalosardo.repository.safras;

import com.example.fazendalosardo.model.safras.ProdutoSafra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoSafraRepository extends JpaRepository<ProdutoSafra, Long> {
    Page<ProdutoSafra> findBySafraId(Long safraId, Pageable pageable);
    List<ProdutoSafra> findBySafraId(Long safraId);
}
