package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.equipamento.EquipamentoRequest;
import com.example.fazendalosardo.dto.equipamento.EquipamentoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EquipamentoService {
    EquipamentoResponse salvar(EquipamentoRequest request, String numeroUsuarioLogado);
    EquipamentoResponse atualizar(Long id, EquipamentoRequest request, String numeroUsuarioLogado);
    Page<EquipamentoResponse> buscarPorFazenda(Long fazendaId, Pageable pageable, String numeroUsuarioLogado);
    EquipamentoResponse buscarPorId(Long id, String numeroUsuarioLogado);
    void inativar(Long id, String numeroUsuarioLogado);
}