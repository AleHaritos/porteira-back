package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.LoteRequest;
import com.example.fazendalosardo.dto.animais.LoteResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Lote;
import com.example.fazendalosardo.repository.animais.AnimalRepository;
import com.example.fazendalosardo.repository.animais.LoteRepository;
import com.example.fazendalosardo.service.LoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoteServiceImpl implements LoteService {

    private final LoteRepository loteRepository;
    private final AnimalRepository animalRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    @Transactional
    public LoteResponse salvar(LoteRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Lote lote = new Lote();
        lote.setNome(request.nome());
        lote.setObservacoes(request.observacoes());
        lote.setFazenda(fazenda);
        lote.setAtivo(true);

        return toResponse(loteRepository.save(lote));
    }

    @Override
    @Transactional
    public LoteResponse atualizar(Long id, LoteRequest request, String numeroUsuarioLogado) {
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lote não encontrado"));

        fazendaAcessoHelper.validarAcesso(lote.getFazenda(), numeroUsuarioLogado);

        lote.setNome(request.nome());
        lote.setObservacoes(request.observacoes());

        return toResponse(lote);
    }

    @Override
    public List<LoteResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return loteRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void inativar(Long id, String numeroUsuarioLogado) {
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
        fazendaAcessoHelper.validarAcesso(lote.getFazenda(), numeroUsuarioLogado);
        lote.setAtivo(false);
    }

    private LoteResponse toResponse(Lote lote) {
        long quantidadeAnimais = animalRepository.countByLoteIdAndAtivoTrue(lote.getId());
        return new LoteResponse(
                lote.getId(),
                lote.getNome(),
                lote.getObservacoes(),
                lote.getAtivo(),
                quantidadeAnimais
        );
    }
}