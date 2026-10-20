package com.example.fazendalosardo.repository.safras;

import com.example.fazendalosardo.model.safras.CustoFixoTalhao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CustoFixoTalhaoRepository extends JpaRepository<CustoFixoTalhao, Long> {

    List<CustoFixoTalhao> findByTalhaoIdOrderByIdAsc(Long talhaoId);

    @Query("SELECT COALESCE(SUM(c.valor), 0) FROM CustoFixoTalhao c WHERE c.talhao.id = :talhaoId")
    BigDecimal somarPorTalhao(@Param("talhaoId") Long talhaoId);
}