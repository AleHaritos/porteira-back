package com.example.fazendalosardo.repository.veiculos;

import com.example.fazendalosardo.model.Posto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostoRepository extends JpaRepository<Posto, Long> {
    List<Posto> findByFazendaIdAndAtivoTrueOrderByNome(Long fazendaId);
    Page<Posto> findByFazendaIdAndAtivoTrueOrderByNome(Long fazendaId, Pageable pageable);
}