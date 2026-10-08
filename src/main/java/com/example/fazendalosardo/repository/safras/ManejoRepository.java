package com.example.fazendalosardo.repository.safras;

import com.example.fazendalosardo.model.safras.Manejo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManejoRepository extends JpaRepository<Manejo, Long> {
    Page<Manejo> findByTalhaoId(Long talhaoId, Pageable pageable);
    List<Manejo> findByTalhaoId(Long talhaoId);
}
