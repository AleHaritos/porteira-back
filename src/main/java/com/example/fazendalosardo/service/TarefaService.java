package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.tarefa.*;

import java.util.List;

public interface TarefaService {
    TarefaResponse salvar(TarefaRequest request, String numeroUsuarioLogado);
    TarefaResponse atualizar(Long id, TarefaRequest request, String numeroUsuarioLogado);
    TarefaResponse atualizarStatus(Long id, TarefaStatusRequest request, String numeroUsuarioLogado);
    void excluir(Long id, String numeroUsuarioLogado);
    List<TarefaResponse> buscarPorFazenda(Long fazendaId, String numeroUsuarioLogado);

    SubTarefaResponse adicionarSubtarefa(Long tarefaId, SubTarefaRequest request, String numeroUsuarioLogado);
    SubTarefaResponse alternarSubtarefa(Long subtarefaId, String numeroUsuarioLogado);
    void excluirSubtarefa(Long subtarefaId, String numeroUsuarioLogado);
}