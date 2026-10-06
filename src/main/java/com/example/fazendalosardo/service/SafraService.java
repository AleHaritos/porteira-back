package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.safraDTO.SafraRequest;
import com.example.fazendalosardo.dto.safraDTO.SafraResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SafraService {

    SafraResponse salvar(SafraRequest request);

    Page<SafraResponse> buscarPorFazenda(Long fazendaId, LocalDate dataInicioDe, LocalDate dataInicioAte, Pageable pageable);


}
