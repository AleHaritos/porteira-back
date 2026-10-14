package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    List<Lote> findByFazendaIdAndAtivoTrueOrderByNome(Long fazendaId);
}