package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.equipamento.FornecedorDebitoResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface FornecedorDebitoService {
    Page<FornecedorDebitoResponse> listarPorFornecedorEStatus(Long fornecedorId, StatusParcela status, LocalDate dataInicio, LocalDate dataFim, Pageable pageable, String numeroUsuarioLogado);
}