package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoRequest;
import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ManutencaoEquipamentoService {
    ManutencaoEquipamentoResponse salvar(ManutencaoEquipamentoRequest request, String numeroUsuarioLogado);
    Page<ManutencaoEquipamentoResponse> buscarPorEquipamento(Long equipamentoId, Pageable pageable, String numeroUsuarioLogado);
    List<ManutencaoEquipamentoResponse> listarPendentesPorFornecedor(Long fornecedorId, String numeroUsuarioLogado);
    ManutencaoEquipamentoResponse darBaixa(Long manutencaoId, LocalDate dataBaixa, String numeroUsuarioLogado);
    Page<ManutencaoEquipamentoResponse> buscarPorFazenda(Long fazendaId, Long equipamentoId, LocalDate dataInicio, LocalDate dataFim,
                                                         Pageable pageable, String numeroUsuarioLogado);
    List<ManutencaoEquipamentoResponse> listarPorFornecedorEStatus(Long fornecedorId, StatusParcela status, String numeroUsuarioLogado);
}