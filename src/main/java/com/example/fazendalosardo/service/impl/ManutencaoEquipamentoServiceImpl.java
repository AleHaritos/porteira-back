package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoRequest;
import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.FormaPagamentoManutencao;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.equipamento.EquipamentoRepository;
import com.example.fazendalosardo.repository.equipamento.FornecedorRepository;
import com.example.fazendalosardo.repository.equipamento.ManutencaoEquipamentoRepository;
import com.example.fazendalosardo.service.FornecedorService;
import com.example.fazendalosardo.service.ManutencaoEquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManutencaoEquipamentoServiceImpl implements ManutencaoEquipamentoService {

    private final ManutencaoEquipamentoRepository manutencaoEquipamentoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;
    private final FornecedorService fornecedorService;

    @Override
    public ManutencaoEquipamentoResponse salvar(ManutencaoEquipamentoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Equipamento equipamento = equipamentoRepository.findById(request.equipamentoId())
                .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));

        helper.validarVinculoXor(request.negocioId(), request.safraId());
        Negocio negocio = helper.buscarNegocio(request.negocioId());
        Safra safra = helper.buscarSafra(request.safraId());

        String base = (request.descricao() != null && !request.descricao().isBlank())
                ? request.descricao() : "Manutenção";
        String descricao = "%s - %s".formatted(base, equipamento.getNome());

        ManutencaoEquipamento manutencao = new ManutencaoEquipamento();
        manutencao.setEquipamento(equipamento);
        manutencao.setTipo(request.tipo());
        manutencao.setDescricao(request.descricao());
        manutencao.setData(request.data());
        manutencao.setValor(request.valor());
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

        return ManutencaoEquipamentoResponse.de(manutencaoEquipamentoRepository.save(manutencao));
    }

    @Override
    public Page<ManutencaoEquipamentoResponse> buscarPorEquipamento(Long equipamentoId, Pageable pageable, String numeroUsuarioLogado) {
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));
        fazendaAcessoHelper.validarAcesso(equipamento.getFazenda(), numeroUsuarioLogado);
        return manutencaoEquipamentoRepository.buscarPorEquipamento(equipamentoId, pageable).map(ManutencaoEquipamentoResponse::de);
    }

    @Override
    public List<ManutencaoEquipamentoResponse> listarPendentesPorFornecedor(Long fornecedorId, String numeroUsuarioLogado) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado"));
        fazendaAcessoHelper.validarAcesso(fornecedor.getFazenda(), numeroUsuarioLogado);
        return manutencaoEquipamentoRepository.listarPendentesPorFornecedor(fornecedorId, StatusParcela.PENDENTE)
                .stream().map(ManutencaoEquipamentoResponse::de).toList();
    }

    @Override
    public ManutencaoEquipamentoResponse darBaixa(Long manutencaoId, LocalDate dataBaixa, String numeroUsuarioLogado) {
        ManutencaoEquipamento manutencao = manutencaoEquipamentoRepository.findById(manutencaoId)
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
        String descricao = "%s - %s".formatted(descricaoBase, manutencao.getEquipamento().getNome());

        Transacao transacao = helper.gerarTransacaoGasto(
                manutencao.getFazenda(), manutencao.getNegocio(), manutencao.getSafra(), manutencao.getValor(), dataEfetiva, descricao);

        manutencao.setTransacao(transacao);
        manutencao.setStatusPagamento(StatusParcela.BAIXADO);
        manutencao.setDataBaixa(dataEfetiva);

        fornecedorService.baixarDivida(manutencao.getFornecedor().getId(), manutencao.getValor(), numeroUsuarioLogado);

        return ManutencaoEquipamentoResponse.de(manutencaoEquipamentoRepository.save(manutencao));
    }
}