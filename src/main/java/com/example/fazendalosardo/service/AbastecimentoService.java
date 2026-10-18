package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.veiculos.AbastecimentoRequest;
import com.example.fazendalosardo.dto.veiculos.AbastecimentoResponse;
import com.example.fazendalosardo.dto.veiculos.AbastecimentoResumoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AbastecimentoService {
    AbastecimentoResponse salvar(AbastecimentoRequest request, String numeroUsuarioLogado);

    Page<AbastecimentoResponse> buscarPorVeiculo(Long veiculoId, Pageable pageable, String numeroUsuarioLogado);

    Page<AbastecimentoResponse> buscarPorFazenda(Long fazendaId, Long veiculoId, LocalDate dataInicio, LocalDate dataFim,
                                                 Long negocioId, Long safraId, Pageable pageable, String numeroUsuarioLogado);

    AbastecimentoResumoResponse buscarResumo(Long fazendaId, Long veiculoId, LocalDate dataInicio, LocalDate dataFim,
                                             Long negocioId, Long safraId, String numeroUsuarioLogado);
}