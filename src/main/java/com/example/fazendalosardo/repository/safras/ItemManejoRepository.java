package com.example.fazendalosardo.repository.safras;

import com.example.fazendalosardo.model.safras.ItemManejo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemManejoRepository extends JpaRepository<ItemManejo, Long> {
    boolean existsByProdutoSafraId(Long produtoSafraId);
}
