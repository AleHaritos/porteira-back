package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.veiculos.EstoqueCombustivelResponse;
import com.example.fazendalosardo.dto.veiculos.ReabastecimentoEstoqueRequest;

import java.util.List;

public interface EstoqueCombustivelService {
    EstoqueCombustivelResponse reabastecer(ReabastecimentoEstoqueRequest request, String numeroUsuarioLogado);
    List<EstoqueCombustivelResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado);
}