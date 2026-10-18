package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.veiculos.AbastecimentoRequest;
import com.example.fazendalosardo.dto.veiculos.AbastecimentoResponse;
import com.example.fazendalosardo.dto.veiculos.AbastecimentoResumoResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.TipoCombustivel;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.veiculos.AbastecimentoRepository;
import com.example.fazendalosardo.repository.veiculos.EstoqueCombustivelRepository;
import com.example.fazendalosardo.repository.veiculos.PostoRepository;
import com.example.fazendalosardo.repository.veiculos.VeiculoRepository;
import com.example.fazendalosardo.service.AbastecimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AbastecimentoServiceImpl implements AbastecimentoService {

    private final AbastecimentoRepository abastecimentoRepository;
    private final VeiculoRepository veiculoRepository;
    private final EstoqueCombustivelRepository estoqueCombustivelRepository;
    private final PostoRepository postoRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;

    @Override
    public AbastecimentoResponse salvar(AbastecimentoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);
        helper.validarVinculoXor(request.negocioId(), request.safraId());

        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado"));

        Negocio negocio = helper.buscarNegocio(request.negocioId());
        Safra safra = helper.buscarSafra(request.safraId());

        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setVeiculo(veiculo);
        abastecimento.setData(request.data());
        abastecimento.setLitros(request.litros());
        abastecimento.setTipoCombustivel(request.tipoCombustivel());
        abastecimento.setLocalAbastecimento(request.localAbastecimento());
        abastecimento.setObservacao(request.observacao());
        abastecimento.setFazenda(fazenda);
        abastecimento.setNegocio(negocio);
        abastecimento.setSafra(safra);
        abastecimento.setAtivo(true);

        switch (request.localAbastecimento()) {
            case POSTO_CASUAL -> processarPostoCasual(abastecimento, request, veiculo, fazenda, negocio, safra);
            case ESTOQUE_FAZENDA -> processarEstoqueFazenda(abastecimento, request, veiculo, fazenda, negocio, safra);
            case POSTO_CREDENCIADO -> processarPostoCredenciado(abastecimento, request);
        }

        return AbastecimentoResponse.de(abastecimentoRepository.save(abastecimento));
    }

    private void processarPostoCasual(Abastecimento abastecimento, AbastecimentoRequest request, Veiculo veiculo,
                                      Fazenda fazenda, Negocio negocio, Safra safra) {
        if (request.valorPorLitro() == null || request.valorPorLitro().signum() <= 0) {
            throw new BusinessException("Informe o valor por litro pago no posto casual");
        }
        BigDecimal valorTotal = request.litros().multiply(request.valorPorLitro());
        String descricao = "Abastecimento - %s (%s)".formatted(veiculo.getNome(), formatarCombustivel(request.tipoCombustivel()));
        Transacao transacao = helper.gerarTransacaoGasto(fazenda, negocio, safra, valorTotal, request.data(), descricao);

        abastecimento.setValorPorLitro(request.valorPorLitro());
        abastecimento.setValorTotal(valorTotal);
        abastecimento.setTransacao(transacao);
    }

    @Override
    public Page<AbastecimentoResponse> buscarPorFazenda(Long fazendaId, Long veiculoId, LocalDate dataInicio, LocalDate dataFim,
                                                        Long negocioId, Long safraId, Pageable pageable, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return abastecimentoRepository.buscarPorFazenda(fazendaId, veiculoId, dataInicio, dataFim, negocioId, safraId, pageable)
                .map(AbastecimentoResponse::de);
    }

    @Override
    public AbastecimentoResumoResponse buscarResumo(Long fazendaId, Long veiculoId, LocalDate dataInicio, LocalDate dataFim,
                                                    Long negocioId, Long safraId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return abastecimentoRepository.buscarResumo(fazendaId, veiculoId, dataInicio, dataFim, negocioId, safraId);
    }

    private void processarEstoqueFazenda(Abastecimento abastecimento, AbastecimentoRequest request, Veiculo veiculo,
                                         Fazenda fazenda, Negocio negocio, Safra safra) {
        EstoqueCombustivel estoque = estoqueCombustivelRepository
                .findByFazendaIdAndTipoCombustivel(fazenda.getId(), request.tipoCombustivel())
                .orElseThrow(() -> new BusinessException("Não há estoque cadastrado para esse combustível"));

        if (estoque.getQuantidadeLitros().compareTo(request.litros()) < 0) {
            throw new BusinessException("Estoque insuficiente: disponível %s L".formatted(estoque.getQuantidadeLitros()));
        }

        BigDecimal valorPorLitroRef = estoque.getUltimoValorPorLitro();
        if (valorPorLitroRef == null || valorPorLitroRef.signum() <= 0) {
            throw new BusinessException("Estoque sem valor por litro definido");
        }

        estoque.setQuantidadeLitros(estoque.getQuantidadeLitros().subtract(request.litros()));
        estoqueCombustivelRepository.save(estoque);

        BigDecimal valorTotal = request.litros().multiply(valorPorLitroRef);
        String descricao = "Abastecimento (estoque) - %s (%s)".formatted(veiculo.getNome(), formatarCombustivel(request.tipoCombustivel()));
        Transacao transacao = helper.gerarTransacaoGasto(fazenda, negocio, safra, valorTotal, request.data(), descricao);

        abastecimento.setValorPorLitro(valorPorLitroRef);
        abastecimento.setValorTotal(valorTotal);
        abastecimento.setTransacao(transacao);
    }

    private void processarPostoCredenciado(Abastecimento abastecimento, AbastecimentoRequest request) {
        if (request.postoId() == null) {
            throw new BusinessException("Selecione o posto credenciado");
        }
        if (request.valorPorLitro() == null || request.valorPorLitro().signum() <= 0) {
            throw new BusinessException("Informe o valor por litro combinado com o posto");
        }

        Posto posto = postoRepository.findById(request.postoId())
                .orElseThrow(() -> new NotFoundException("Posto não encontrado"));

        BigDecimal valorTotal = request.litros().multiply(request.valorPorLitro());
        posto.setSaldoDevedor(posto.getSaldoDevedor().add(valorTotal));
        postoRepository.save(posto);

        abastecimento.setPosto(posto);
        abastecimento.setValorPorLitro(request.valorPorLitro());
        abastecimento.setValorTotal(valorTotal);
        // sem Transacao: entra como dívida corrente do posto, paga depois
    }

    @Override
    public Page<AbastecimentoResponse> buscarPorVeiculo(Long veiculoId, Pageable pageable, String numeroUsuarioLogado) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
        fazendaAcessoHelper.validarAcesso(veiculo.getFazenda(), numeroUsuarioLogado);
        return abastecimentoRepository.buscarPorVeiculo(veiculoId, pageable).map(AbastecimentoResponse::de);
    }

    private String formatarCombustivel(TipoCombustivel tipo) {
        return switch (tipo) {
            case DIESEL -> "Diesel";
            case GASOLINA -> "Gasolina";
            case ETANOL -> "Etanol";
        };
    }
}