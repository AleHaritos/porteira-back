package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.veiculos.PostoRequest;
import com.example.fazendalosardo.dto.veiculos.PostoResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Posto;
import com.example.fazendalosardo.repository.veiculos.AbastecimentoRepository;
import com.example.fazendalosardo.repository.veiculos.PostoRepository;
import com.example.fazendalosardo.service.PostoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostoServiceImpl implements PostoService {

    private final PostoRepository postoRepository;
    private final AbastecimentoRepository abastecimentoRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    public PostoResponse salvar(PostoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Posto posto = new Posto();
        posto.setNome(request.nome());
        posto.setFazenda(fazenda);
        posto.setObservacao(request.observacao());
        posto.setAtivo(true);

        return PostoResponse.de(postoRepository.save(posto), BigDecimal.ZERO);
    }

    @Override
    public Page<PostoResponse> listarPorFazenda(Long fazendaId, Pageable pageable, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return postoRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId, pageable)
                .map(posto -> PostoResponse.de(posto, abastecimentoRepository.somarPendentePorPosto(posto.getId())));
    }

    @Override
    public List<PostoResponse> listarTodosPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return postoRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId).stream()
                .map(posto -> PostoResponse.de(posto, abastecimentoRepository.somarPendentePorPosto(posto.getId())))
                .toList();
    }

    @Override
    public void excluir(Long postoId, String numeroUsuarioLogado) {
        Posto posto = postoRepository.findById(postoId)
                .orElseThrow(() -> new NotFoundException("Posto não encontrado"));
        fazendaAcessoHelper.validarAcesso(posto.getFazenda(), numeroUsuarioLogado);
        posto.setAtivo(false);
        postoRepository.save(posto);
    }
}