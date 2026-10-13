package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.veiculos.AbastecimentoRequest;
import com.example.fazendalosardo.dto.veiculos.AbastecimentoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AbastecimentoService {
    AbastecimentoResponse salvar(AbastecimentoRequest request, String numeroUsuarioLogado);
    Page<AbastecimentoResponse> buscarPorVeiculo(Long veiculoId, Pageable pageable, String numeroUsuarioLogado);
}