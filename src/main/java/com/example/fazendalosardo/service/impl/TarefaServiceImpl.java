package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.tarefa.*;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.SubTarefa;
import com.example.fazendalosardo.model.Tarefa;
import com.example.fazendalosardo.model.enums.StatusTarefa;
import com.example.fazendalosardo.repository.SubTarefaRepository;
import com.example.fazendalosardo.repository.TarefaRepository;
import com.example.fazendalosardo.service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaServiceImpl implements TarefaService {

    private final TarefaRepository tarefaRepository;
    private final SubTarefaRepository subTarefaRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    public TarefaResponse salvar(TarefaRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Integer proximaOrdem = tarefaRepository.buscarPorFazenda(request.fazendaId()).stream()
                .filter(t -> t.getStatus() == StatusTarefa.A_FAZER)
                .map(Tarefa::getOrdem)
                .max(Integer::compareTo)
                .map(o -> o + 1)
                .orElse(0);

        Tarefa tarefa = new Tarefa();
        tarefa.setFazenda(fazenda);
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setPrioridade(request.prioridade());
        tarefa.setPrazo(request.prazo());
        tarefa.setOrdem(proximaOrdem);

        if (request.subtarefas() != null) {
            int ordem = 0;
            for (String titulo : request.subtarefas()) {
                if (titulo == null || titulo.isBlank()) continue;
                SubTarefa subtarefa = new SubTarefa();
                subtarefa.setTarefa(tarefa);
                subtarefa.setTitulo(titulo.trim());
                subtarefa.setOrdem(ordem++);
                tarefa.getSubtarefas().add(subtarefa);
            }
        }

        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    @Override
    public TarefaResponse atualizar(Long id, TarefaRequest request, String numeroUsuarioLogado) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
        fazendaAcessoHelper.validarAcesso(tarefa.getFazenda(), numeroUsuarioLogado);

        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setPrioridade(request.prioridade());
        tarefa.setPrazo(request.prazo());

        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    @Override
    public TarefaResponse atualizarStatus(Long id, TarefaStatusRequest request, String numeroUsuarioLogado) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
        fazendaAcessoHelper.validarAcesso(tarefa.getFazenda(), numeroUsuarioLogado);

        tarefa.setStatus(request.status());
        tarefa.setOrdem(request.ordem());

        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    @Override
    public void excluir(Long id, String numeroUsuarioLogado) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
        fazendaAcessoHelper.validarAcesso(tarefa.getFazenda(), numeroUsuarioLogado);
        tarefaRepository.delete(tarefa);
    }

    @Override
    public List<TarefaResponse> buscarPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return tarefaRepository.buscarPorFazenda(fazendaId).stream().map(TarefaResponse::de).toList();
    }

    @Override
    public SubTarefaResponse adicionarSubtarefa(Long tarefaId, SubTarefaRequest request, String numeroUsuarioLogado) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));
        fazendaAcessoHelper.validarAcesso(tarefa.getFazenda(), numeroUsuarioLogado);

        Integer proximaOrdem = tarefa.getSubtarefas().stream()
                .map(SubTarefa::getOrdem)
                .max(Integer::compareTo)
                .map(o -> o + 1)
                .orElse(0);

        SubTarefa subtarefa = new SubTarefa();
        subtarefa.setTarefa(tarefa);
        subtarefa.setTitulo(request.titulo());
        subtarefa.setOrdem(proximaOrdem);

        return SubTarefaResponse.de(subTarefaRepository.save(subtarefa));
    }

    @Override
    public SubTarefaResponse alternarSubtarefa(Long subtarefaId, String numeroUsuarioLogado) {
        SubTarefa subtarefa = subTarefaRepository.findById(subtarefaId)
                .orElseThrow(() -> new NotFoundException("Subtarefa não encontrada"));
        fazendaAcessoHelper.validarAcesso(subtarefa.getTarefa().getFazenda(), numeroUsuarioLogado);

        subtarefa.setConcluida(!subtarefa.getConcluida());

        return SubTarefaResponse.de(subTarefaRepository.save(subtarefa));
    }

    @Override
    public void excluirSubtarefa(Long subtarefaId, String numeroUsuarioLogado) {
        SubTarefa subtarefa = subTarefaRepository.findById(subtarefaId)
                .orElseThrow(() -> new NotFoundException("Subtarefa não encontrada"));
        fazendaAcessoHelper.validarAcesso(subtarefa.getTarefa().getFazenda(), numeroUsuarioLogado);
        subTarefaRepository.delete(subtarefa);
    }
}