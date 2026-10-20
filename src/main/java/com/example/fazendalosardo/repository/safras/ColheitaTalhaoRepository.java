package com.example.fazendalosardo.repository.safras;

import com.example.fazendalosardo.model.safras.ColheitaTalhao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColheitaTalhaoRepository extends JpaRepository<ColheitaTalhao, Long> {
    List<ColheitaTalhao> findByTalhaoIdOrderByIdAsc(Long talhaoId);
}