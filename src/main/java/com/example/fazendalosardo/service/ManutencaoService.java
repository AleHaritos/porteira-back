package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.veiculos.ManutencaoRequest;
import com.example.fazendalosardo.dto.veiculos.ManutencaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManutencaoService {
    ManutencaoResponse salvar(ManutencaoRequest request, String numeroUsuarioLogado);
    Page<ManutencaoResponse> buscarPorVeiculo(Long veiculoId, Pageable pageable, String numeroUsuarioLogado);
}