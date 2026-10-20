package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoRequest;
import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ManutencaoEquipamentoService {
    ManutencaoEquipamentoResponse salvar(ManutencaoEquipamentoRequest request, String numeroUsuarioLogado);
    Page<ManutencaoEquipamentoResponse> buscarPorEquipamento(Long equipamentoId, Pageable pageable, String numeroUsuarioLogado);
    List<ManutencaoEquipamentoResponse> listarPendentesPorFornecedor(Long fornecedorId, String numeroUsuarioLogado);
    ManutencaoEquipamentoResponse darBaixa(Long manutencaoId, Long negocioId, Long safraId, LocalDate dataBaixa, String numeroUsuarioLogado);
}