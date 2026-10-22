package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.Piquete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PiqueteRepository extends JpaRepository<Piquete, Long> {
    List<Piquete> findByFazendaIdAndAtivoTrueOrderByNome(Long fazendaId);
}