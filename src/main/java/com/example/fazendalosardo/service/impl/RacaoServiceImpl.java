package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.RacaoIngredienteResponse;
import com.example.fazendalosardo.dto.animais.RacaoRequest;
import com.example.fazendalosardo.dto.animais.RacaoResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Ingrediente;
import com.example.fazendalosardo.model.Racao;
import com.example.fazendalosardo.model.RacaoIngrediente;
import com.example.fazendalosardo.repository.animais.IngredienteRepository;
import com.example.fazendalosardo.repository.animais.RacaoRepository;
import com.example.fazendalosardo.service.RacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RacaoServiceImpl implements RacaoService {

    private final RacaoRepository racaoRepository;
    private final IngredienteRepository ingredienteRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    @Transactional
    public RacaoResponse salvar(RacaoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Racao racao = new Racao();
        racao.setNome(request.nome());
        racao.setFazenda(fazenda);

        montarIngredientes(racao, request.ingredientes());

        return toResponse(racaoRepository.save(racao));
    }

    @Override
    @Transactional
    public RacaoResponse atualizar(Long id, RacaoRequest request, String numeroUsuarioLogado) {
        Racao racao = racaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ração não encontrada"));
        fazendaAcessoHelper.validarAcesso(racao.getFazenda(), numeroUsuarioLogado);

        racao.setNome(request.nome());
        racao.getIngredientes().clear();
        montarIngredientes(racao, request.ingredientes());

        return toResponse(racao);
    }

    private void montarIngredientes(Racao racao, List<RacaoRequest.ItemIngrediente> itens) {
        for (RacaoRequest.ItemIngrediente item : itens) {
            Ingrediente ingrediente = ingredienteRepository.findById(item.ingredienteId())
                    .orElseThrow(() -> new NotFoundException("Ingrediente não encontrado"));

            RacaoIngrediente ri = new RacaoIngrediente();
            ri.setRacao(racao);
            ri.setIngrediente(ingrediente);
            ri.setQuantidade(item.quantidade());
            racao.getIngredientes().add(ri);
        }
    }

    @Override
    public List<RacaoResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return racaoRepository.buscarAtivasPorFazenda(fazendaId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void inativar(Long id, String numeroUsuarioLogado) {
        Racao racao = racaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ração não encontrada"));
        fazendaAcessoHelper.validarAcesso(racao.getFazenda(), numeroUsuarioLogado);
        racao.setAtivo(false);
    }

    private RacaoResponse toResponse(Racao racao) {
        List<RacaoIngredienteResponse> ingredientes = racao.getIngredientes().stream()
                .map(ri -> new RacaoIngredienteResponse(
                        ri.getIngrediente().getId(),
                        ri.getIngrediente().getNome(),
                        ri.getQuantidade(),
                        ri.getIngrediente().getValorUnitario(),
                        ri.getQuantidade().multiply(ri.getIngrediente().getValorUnitario())
                ))
                .toList();

        BigDecimal custoTotal = ingredientes.stream()
                .map(RacaoIngredienteResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RacaoResponse(racao.getId(), racao.getNome(), racao.getAtivo(), custoTotal, ingredientes);
    }
}