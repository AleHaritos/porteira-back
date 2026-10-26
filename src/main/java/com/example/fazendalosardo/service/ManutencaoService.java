package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.veiculos.ManutencaoRequest;
import com.example.fazendalosardo.dto.veiculos.ManutencaoResponse;
import com.example.fazendalosardo.dto.veiculos.ManutencaoResumoResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ManutencaoService {
    ManutencaoResponse salvar(ManutencaoRequest request, String numeroUsuarioLogado);
    Page<ManutencaoResponse> buscarPorVeiculo(Long veiculoId, Pageable pageable, String numeroUsuarioLogado);
    List<ManutencaoResponse> listarPendentesPorFornecedor(Long fornecedorId, String numeroUsuarioLogado);
    ManutencaoResponse darBaixa(Long manutencaoId, LocalDate dataBaixa, String numeroUsuarioLogado);
    Page<ManutencaoResponse> buscarPorVeiculoEPeriodo(Long veiculoId, LocalDate dataInicio, LocalDate dataFim,
                                                      Pageable pageable, String numeroUsuarioLogado);

    ManutencaoResumoResponse buscarResumoPorVeiculo(Long veiculoId, LocalDate dataInicio, LocalDate dataFim,
                                                    String numeroUsuarioLogado);

    Page<ManutencaoResponse> buscarPorFazenda(Long fazendaId, Long veiculoId, LocalDate dataInicio, LocalDate dataFim,
                                              Pageable pageable, String numeroUsuarioLogado);

    List<ManutencaoResponse> listarPorFornecedorEStatus(Long fornecedorId, StatusParcela status, String numeroUsuarioLogado);
}