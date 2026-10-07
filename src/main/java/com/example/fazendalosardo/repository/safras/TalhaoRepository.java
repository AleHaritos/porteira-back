package com.example.fazendalosardo.repository.safras;

import com.example.fazendalosardo.model.safras.Talhao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TalhaoRepository extends JpaRepository<Talhao, Long> {
    Page<Talhao> findBySafraId(Long safraId, Pageable pageable);
    List<Talhao> findBySafraId(Long safraId);

    @Query("SELECT COALESCE(SUM(t.areaHectares), 0) FROM Talhao t WHERE t.safra.id = :safraId")
    BigDecimal somarAreaPorSafra(@Param("safraId") Long safraId);
}
