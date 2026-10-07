package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.safraDTO.SafraRequest;
import com.example.fazendalosardo.dto.safraDTO.SafraResponse;
import com.example.fazendalosardo.dto.safraDTO.SafraUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SafraService {

    SafraResponse salvar(SafraRequest request);

    Page<SafraResponse> buscarPorFazenda(Long fazendaId, String anoAgricola, Pageable pageable);

    SafraResponse atualizar(Long id, SafraUpdateRequest request);

}
