package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.veiculos.ManutencaoRequest;
import com.example.fazendalosardo.dto.veiculos.ManutencaoResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.FormaPagamentoManutencao;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.equipamento.FornecedorRepository;
import com.example.fazendalosardo.repository.veiculos.ManutencaoRepository;
import com.example.fazendalosardo.repository.veiculos.VeiculoRepository;
import com.example.fazendalosardo.service.FornecedorService;
import com.example.fazendalosardo.service.ManutencaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManutencaoServiceImpl implements ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;
    private final VeiculoRepository veiculoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;
    private final FornecedorService fornecedorService;

    @Override
    public ManutencaoResponse salvar(ManutencaoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado"));


        helper.validarVinculoXor(request.negocioId(), request.safraId());
        Negocio negocio = helper.buscarNegocio(request.negocioId());
        Safra safra = helper.buscarSafra(request.safraId());

        String base = (request.descricao() != null && !request.descricao().isBlank())
                ? request.descricao() : "Manutenção";
        String descricao = "%s - %s".formatted(base, veiculo.getNome());

        Manutencao manutencao = new Manutencao();
        manutencao.setVeiculo(veiculo);
        manutencao.setTipo(request.tipo());
        manutencao.setDescricao(request.descricao());
        manutencao.setData(request.data());
        manutencao.setValor(request.valor());
        manutencao.setKmAtual(request.kmAtual());
        manutencao.setObservacao(request.observacao());
        manutencao.setFazenda(fazenda);
        manutencao.setNegocio(negocio);
        manutencao.setSafra(safra);
        manutencao.setFormaPagamento(request.formaPagamento());
        manutencao.setAtivo(true);

        if (request.formaPagamento() == FormaPagamentoManutencao.A_VISTA) {
            Transacao transacao = helper.gerarTransacaoGasto(fazenda, negocio, safra, request.valor(), request.data(), descricao);
            manutencao.setTransacao(transacao);
            manutencao.setStatusPagamento(StatusParcela.BAIXADO);
        } else {
            if (request.fornecedorId() == null) {
                throw new BusinessException("Informe o fornecedor para manutenção no fiado");
            }
            Fornecedor fornecedor = fornecedorService.acumularDivida(request.fornecedorId(), request.valor(), numeroUsuarioLogado);
            manutencao.setFornecedor(fornecedor);
            manutencao.setStatusPagamento(StatusParcela.PENDENTE);
        }

        return ManutencaoResponse.de(manutencaoRepository.save(manutencao));
    }

    @Override
    public Page<ManutencaoResponse> buscarPorVeiculo(Long veiculoId, Pageable pageable, String numeroUsuarioLogado) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
        fazendaAcessoHelper.validarAcesso(veiculo.getFazenda(), numeroUsuarioLogado);
        return manutencaoRepository.buscarPorVeiculo(veiculoId, pageable).map(ManutencaoResponse::de);
    }

    @Override
    public List<ManutencaoResponse> listarPendentesPorFornecedor(Long fornecedorId, String numeroUsuarioLogado) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado"));
        fazendaAcessoHelper.validarAcesso(fornecedor.getFazenda(), numeroUsuarioLogado);
        return manutencaoRepository.listarPendentesPorFornecedor(fornecedorId, StatusParcela.PENDENTE)
                .stream().map(ManutencaoResponse::de).toList();
    }

    @Override
    public ManutencaoResponse darBaixa(Long manutencaoId, LocalDate dataBaixa, String numeroUsuarioLogado) {
        Manutencao manutencao = manutencaoRepository.findById(manutencaoId)
                .orElseThrow(() -> new NotFoundException("Manutenção não encontrada"));
        fazendaAcessoHelper.validarAcesso(manutencao.getFazenda(), numeroUsuarioLogado);

        if (manutencao.getFormaPagamento() != FormaPagamentoManutencao.FORNECEDOR_CREDENCIADO) {
            throw new BusinessException("Esta manutenção não está no fiado");
        }
        if (manutencao.getStatusPagamento() == StatusParcela.BAIXADO) {
            throw new BusinessException("Esta manutenção já foi paga");
        }

        LocalDate dataEfetiva = dataBaixa != null ? dataBaixa : LocalDate.now();
        String descricaoBase = (manutencao.getDescricao() != null && !manutencao.getDescricao().isBlank())
                ? manutencao.getDescricao() : "Manutenção";
        String descricao = "%s - %s".formatted(descricaoBase, manutencao.getVeiculo().getNome());

        Transacao transacao = helper.gerarTransacaoGasto(
                manutencao.getFazenda(), manutencao.getNegocio(), manutencao.getSafra(), manutencao.getValor(), dataEfetiva, descricao);

        manutencao.setTransacao(transacao);
        manutencao.setStatusPagamento(StatusParcela.BAIXADO);
        manutencao.setDataBaixa(dataEfetiva);

        fornecedorService.baixarDivida(manutencao.getFornecedor().getId(), manutencao.getValor(), numeroUsuarioLogado);

        return ManutencaoResponse.de(manutencaoRepository.save(manutencao));
    }
}