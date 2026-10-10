package com.example.fazendalosardo.repository.financeiro;

import com.example.fazendalosardo.model.ContaPendente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaPendenteRepository extends JpaRepository<ContaPendente, Long> {
}