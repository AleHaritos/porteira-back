package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.veiculos.ManutencaoParceladaRequest;
import com.example.fazendalosardo.dto.veiculos.ManutencaoParceladaResponse;
import com.example.fazendalosardo.dto.veiculos.ParcelaManutencaoResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.equipamento.EquipamentoRepository;
import com.example.fazendalosardo.repository.equipamento.FornecedorRepository;
import com.example.fazendalosardo.repository.veiculos.ManutencaoParceladaRepository;
import com.example.fazendalosardo.repository.veiculos.ParcelaManutencaoRepository;
import com.example.fazendalosardo.repository.veiculos.VeiculoRepository;
import com.example.fazendalosardo.service.FornecedorService;
import com.example.fazendalosardo.service.ManutencaoParceladaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManutencaoParceladaServiceImpl implements ManutencaoParceladaService {

    private final ManutencaoParceladaRepository manutencaoParceladaRepository;
    private final ParcelaManutencaoRepository parcelaManutencaoRepository;
    private final VeiculoRepository veiculoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;
    private final FornecedorService fornecedorService;

    @Override
    public ManutencaoParceladaResponse salvar(ManutencaoParceladaRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        boolean temVeiculo = request.veiculoId() != null;
        boolean temEquipamento = request.equipamentoId() != null;
        if (temVeiculo == temEquipamento) {
            throw new BusinessException("Informe exatamente um veículo ou um equipamento");
        }

        Veiculo veiculo = null;
        Equipamento equipamento = null;

        if (temVeiculo) {
            veiculo = veiculoRepository.findById(request.veiculoId())
                    .orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
        } else {
            equipamento = equipamentoRepository.findById(request.equipamentoId())
                    .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));
        }

        Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado"));

        helper.validarVinculoXor(request.negocioId(), request.safraId());
        Negocio negocio = helper.buscarNegocio(request.negocioId());
        Safra safra = helper.buscarSafra(request.safraId());

        if (request.valoresParcelas() == null || request.valoresParcelas().isEmpty()) {
            throw new BusinessException("Informe ao menos uma parcela");
        }

        BigDecimal valorTotal = request.valoresParcelas().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        ManutencaoParcelada manutencao = new ManutencaoParcelada();
        manutencao.setVeiculo(veiculo);
        manutencao.setEquipamento(equipamento);
        manutencao.setTipo(request.tipo());
        manutencao.setDescricao(request.descricao());
        manutencao.setValorTotal(valorTotal);
        manutencao.setNumeroParcelas(request.valoresParcelas().size());
        manutencao.setKmAtual(request.kmAtual());
        manutencao.setObservacao(request.observacao());
        manutencao.setFazenda(fazenda);
        manutencao.setFornecedor(fornecedor);
        manutencao.setNegocio(negocio);
        manutencao.setSafra(safra);
        manutencao.setAtivo(true);
        manutencao.setParcelas(gerarParcelas(manutencao, request.valoresParcelas(), request.primeiraDataVencimento()));

        fornecedorService.acumularDivida(fornecedor.getId(), valorTotal, numeroUsuarioLogado);

        return ManutencaoParceladaResponse.de(manutencaoParceladaRepository.save(manutencao));
    }

    private List<ParcelaManutencao> gerarParcelas(ManutencaoParcelada manutencao, List<BigDecimal> valoresParcelas,
                                                  LocalDate primeiraDataVencimento) {
        List<ParcelaManutencao> parcelas = new ArrayList<>();
        for (int i = 0; i < valoresParcelas.size(); i++) {
            ParcelaManutencao parcela = new ParcelaManutencao();
            parcela.setManutencaoParcelada(manutencao);
            parcela.setNumeroParcela(i + 1);
            parcela.setDataVencimento(primeiraDataVencimento.plusMonths(i));
            parcela.setStatus(StatusParcela.PENDENTE);
            parcela.setValor(valoresParcelas.get(i));
            parcelas.add(parcela);
        }
        return parcelas;
    }

    @Override
    public Page<ParcelaManutencaoResponse> buscarParcelasPorFazenda(Long fazendaId, StatusParcela status, Pageable pageable, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return parcelaManutencaoRepository.buscarPorFazenda(fazendaId, status, pageable).map(ParcelaManutencaoResponse::de);
    }

    @Override
    public ParcelaManutencaoResponse darBaixa(Long parcelaId, LocalDate dataBaixa, String numeroUsuarioLogado) {
        ParcelaManutencao parcela = parcelaManutencaoRepository.findById(parcelaId)
                .orElseThrow(() -> new NotFoundException("Parcela não encontrada"));

        ManutencaoParcelada manutencao = parcela.getManutencaoParcelada();
        fazendaAcessoHelper.validarAcesso(manutencao.getFazenda(), numeroUsuarioLogado);

        if (parcela.getStatus() == StatusParcela.BAIXADO) {
            throw new BusinessException("Esta parcela já foi paga");
        }

        LocalDate dataEfetiva = dataBaixa != null ? dataBaixa : LocalDate.now();
        String ativoNome = manutencao.getVeiculo() != null ? manutencao.getVeiculo().getNome() : manutencao.getEquipamento().getNome();
        String descricaoBase = (manutencao.getDescricao() != null && !manutencao.getDescricao().isBlank())
                ? manutencao.getDescricao() : "Manutenção";
        String descricao = "%s - %s (Parcela %d/%d)".formatted(
                descricaoBase, ativoNome, parcela.getNumeroParcela(), manutencao.getNumeroParcelas());

        Transacao transacao = helper.gerarTransacaoGasto(
                manutencao.getFazenda(), manutencao.getNegocio(), manutencao.getSafra(), parcela.getValor(), dataEfetiva, descricao);

        parcela.setTransacao(transacao);
        parcela.setStatus(StatusParcela.BAIXADO);
        parcela.setDataBaixa(dataEfetiva);

        fornecedorService.baixarDivida(manutencao.getFornecedor().getId(), parcela.getValor(), numeroUsuarioLogado);

        return ParcelaManutencaoResponse.de(parcelaManutencaoRepository.save(parcela));
    }
}