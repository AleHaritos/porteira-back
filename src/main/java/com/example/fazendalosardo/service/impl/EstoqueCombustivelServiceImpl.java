package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.veiculos.EstoqueCombustivelResponse;
import com.example.fazendalosardo.dto.veiculos.ReabastecimentoEstoqueRequest;
import com.example.fazendalosardo.model.EstoqueCombustivel;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.model.enums.TipoCombustivel;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.veiculos.EstoqueCombustivelRepository;
import com.example.fazendalosardo.service.EstoqueCombustivelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueCombustivelServiceImpl implements EstoqueCombustivelService {

    private final EstoqueCombustivelRepository estoqueCombustivelRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;

    @Override
    public EstoqueCombustivelResponse reabastecer(ReabastecimentoEstoqueRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);
        helper.validarVinculoXor(request.negocioId(), request.safraId());

        Negocio negocio = helper.buscarNegocio(request.negocioId());
        Safra safra = helper.buscarSafra(request.safraId());

        BigDecimal valorTotal = request.litros().multiply(request.valorPorLitro());
        String descricao = "Reabastecimento de estoque - %s".formatted(formatarCombustivel(request.tipoCombustivel()));
        helper.gerarTransacaoGasto(fazenda, negocio, safra, valorTotal, request.data(), descricao);

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

        return EstoqueCombustivelResponse.de(estoqueCombustivelRepository.save(estoque));
    }

    @Override
    public List<EstoqueCombustivelResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return estoqueCombustivelRepository.findByFazendaId(fazendaId).stream()
                .map(EstoqueCombustivelResponse::de)
                .toList();
    }

    private String formatarCombustivel(TipoCombustivel tipo) {
        return switch (tipo) {
            case DIESEL -> "Diesel";
            case GASOLINA -> "Gasolina";
            case ETANOL -> "Etanol";
        };
    }
}