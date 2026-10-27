package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.veiculos.EstoqueCombustivelResponse;
import com.example.fazendalosardo.dto.veiculos.ReabastecimentoEstoqueRequest;
import com.example.fazendalosardo.dto.veiculos.ReabastecimentoHistoricoResponse;
import com.example.fazendalosardo.model.EstoqueCombustivel;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.ReabastecimentoCombustivel;
import com.example.fazendalosardo.model.enums.TipoCombustivel;
import com.example.fazendalosardo.repository.veiculos.EstoqueCombustivelRepository;
import com.example.fazendalosardo.repository.veiculos.ReabastecimentoCombustivelRepository;
import com.example.fazendalosardo.service.EstoqueCombustivelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueCombustivelServiceImpl implements EstoqueCombustivelService {

    private final EstoqueCombustivelRepository estoqueCombustivelRepository;
    private final ReabastecimentoCombustivelRepository reabastecimentoCombustivelRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;

    @Override
    public EstoqueCombustivelResponse reabastecer(ReabastecimentoEstoqueRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        EstoqueCombustivel estoque = estoqueCombustivelRepository
                .findByFazendaIdAndTipoCombustivel(fazenda.getId(), request.tipoCombustivel())
                .orElseGet(() -> {
                    EstoqueCombustivel novo = new EstoqueCombustivel();
                    novo.setFazenda(fazenda);
                    novo.setTipoCombustivel(request.tipoCombustivel());
                    novo.setQuantidadeLitros(BigDecimal.ZERO);
                    return novo;
                });

        estoque.setQuantidadeLitros(estoque.getQuantidadeLitros().add(request.litros()));
        estoque.setUltimoValorPorLitro(request.valorPorLitro());
        // sem Transacao aqui: o gasto só é contabilizado quando o combustível é consumido

        EstoqueCombustivel salvo = estoqueCombustivelRepository.save(estoque);

        ReabastecimentoCombustivel historico = new ReabastecimentoCombustivel();
        historico.setFazenda(fazenda);
        historico.setTipoCombustivel(request.tipoCombustivel());
        historico.setLitros(request.litros());
        historico.setValorPorLitro(request.valorPorLitro());
        historico.setData(request.data());
        historico.setNegocio(helper.buscarNegocio(request.negocioId()));
        historico.setSafra(helper.buscarSafra(request.safraId()));
        historico.setObservacao(request.observacao());
        reabastecimentoCombustivelRepository.save(historico);

        return EstoqueCombustivelResponse.de(salvo);
    }

    @Override
    public List<EstoqueCombustivelResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return estoqueCombustivelRepository.findByFazendaId(fazendaId).stream()
                .map(EstoqueCombustivelResponse::de)
                .toList();
    }

    @Override
    public List<ReabastecimentoHistoricoResponse> listarHistorico(Long fazendaId, TipoCombustivel tipoCombustivel,
                                                                  LocalDate dataInicio, LocalDate dataFim,
                                                                  String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return reabastecimentoCombustivelRepository
                .buscarHistorico(fazendaId, tipoCombustivel, dataInicio, dataFim)
                .stream().map(ReabastecimentoHistoricoResponse::de).toList();
    }
}