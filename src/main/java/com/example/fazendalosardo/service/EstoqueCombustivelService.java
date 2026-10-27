package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.veiculos.EstoqueCombustivelResponse;
import com.example.fazendalosardo.dto.veiculos.ReabastecimentoEstoqueRequest;
import com.example.fazendalosardo.dto.veiculos.ReabastecimentoHistoricoResponse;
import com.example.fazendalosardo.model.enums.TipoCombustivel;

import java.time.LocalDate;
import java.util.List;

public interface EstoqueCombustivelService {
    EstoqueCombustivelResponse reabastecer(ReabastecimentoEstoqueRequest request, String numeroUsuarioLogado);

    List<EstoqueCombustivelResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado);

    List<ReabastecimentoHistoricoResponse> listarHistorico(Long fazendaId, TipoCombustivel tipoCombustivel,
                                                           LocalDate dataInicio, LocalDate dataFim, String numeroUsuarioLogado);
}