package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.tarefa.*;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.model.Projeto;
import com.example.fazendalosardo.model.SubTarefa;
import com.example.fazendalosardo.model.Tarefa;
import com.example.fazendalosardo.model.enums.StatusTarefa;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.NegocioRepository;
import com.example.fazendalosardo.repository.ProjetoRepository;
import com.example.fazendalosardo.repository.SubTarefaRepository;
import com.example.fazendalosardo.repository.TarefaRepository;
import com.example.fazendalosardo.repository.safras.SafraRepository;
import com.example.fazendalosardo.service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TarefaServiceImpl implements TarefaService {

    private final TarefaRepository tarefaRepository;
    private final SubTarefaRepository subTarefaRepository;
    private final NegocioRepository negocioRepository;
    private final SafraRepository safraRepository;
    private final ProjetoRepository projetoRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    public TarefaResponse salvar(TarefaRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        validarVinculoUnico(request.negocioId(), request.safraId(), request.projetoId());

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
        aplicarVinculo(tarefa, request.negocioId(), request.safraId(), request.projetoId());

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

        validarVinculoUnico(request.negocioId(), request.safraId(), request.projetoId());

        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setPrioridade(request.prioridade());
        tarefa.setPrazo(request.prazo());
        aplicarVinculo(tarefa, request.negocioId(), request.safraId(), request.projetoId());

        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    private void validarVinculoUnico(Long negocioId, Long safraId, Long projetoId) {
        long preenchidos = Stream.of(negocioId, safraId, projetoId).filter(Objects::nonNull).count();
        if (preenchidos > 1) {
            throw new IllegalArgumentException("Selecione no máximo um vínculo: negócio, safra ou projeto");
        }
    }

    private void aplicarVinculo(Tarefa tarefa, Long negocioId, Long safraId, Long projetoId) {
        if (negocioId != null) {
            Negocio negocio = negocioRepository.findById(negocioId)
                    .orElseThrow(() -> new NotFoundException("Negócio não encontrado"));
            tarefa.setNegocio(negocio);
            tarefa.setSafra(null);
            tarefa.setProjeto(null);
        } else if (safraId != null) {
            Safra safra = safraRepository.findById(safraId)
                    .orElseThrow(() -> new NotFoundException("Safra não encontrada"));
            tarefa.setSafra(safra);
            tarefa.setNegocio(null);
            tarefa.setProjeto(null);
        } else if (projetoId != null) {
            Projeto projeto = projetoRepository.findById(projetoId)
                    .orElseThrow(() -> new NotFoundException("Projeto não encontrado"));
            tarefa.setProjeto(projeto);
            tarefa.setNegocio(null);
            tarefa.setSafra(null);
        } else {
            tarefa.setNegocio(null);
            tarefa.setSafra(null);
            tarefa.setProjeto(null);
        }
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