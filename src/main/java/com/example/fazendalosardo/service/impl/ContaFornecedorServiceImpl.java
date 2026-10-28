package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.equipamento.ContaFornecedorRequest;
import com.example.fazendalosardo.dto.equipamento.ContaFornecedorResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.equipamento.ContaFornecedorRepository;
import com.example.fazendalosardo.repository.equipamento.FornecedorRepository;
import com.example.fazendalosardo.service.ContaFornecedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaFornecedorServiceImpl implements ContaFornecedorService {

    private final ContaFornecedorRepository contaFornecedorRepository;
    private final FornecedorRepository fornecedorRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;

    @Override
    public ContaFornecedorResponse salvar(ContaFornecedorRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado"));

        helper.validarVinculoXor(request.negocioId(), request.safraId());
        Negocio negocio = helper.buscarNegocio(request.negocioId());
        Safra safra = helper.buscarSafra(request.safraId());

        ContaFornecedor conta = new ContaFornecedor();
        conta.setFornecedor(fornecedor);
        conta.setFazenda(fazenda);
        conta.setDescricao(request.descricao());
        conta.setData(request.data());
        conta.setValor(request.valor());
        conta.setNegocio(negocio);
        conta.setSafra(safra);
        conta.setStatusPagamento(StatusParcela.PENDENTE);

        return ContaFornecedorResponse.de(contaFornecedorRepository.save(conta));
    }

    @Override
    public List<ContaFornecedorResponse> listarPendentesPorFornecedor(Long fornecedorId, String numeroUsuarioLogado) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado"));
        fazendaAcessoHelper.validarAcesso(fornecedor.getFazenda(), numeroUsuarioLogado);
        return contaFornecedorRepository.listarPendentesPorFornecedor(fornecedorId, StatusParcela.PENDENTE)
                .stream().map(ContaFornecedorResponse::de).toList();
    }

    @Override
    public ContaFornecedorResponse darBaixa(Long contaId, LocalDate dataBaixa, String numeroUsuarioLogado) {
        ContaFornecedor conta = contaFornecedorRepository.findById(contaId)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada"));
        fazendaAcessoHelper.validarAcesso(conta.getFazenda(), numeroUsuarioLogado);

        if (conta.getStatusPagamento() == StatusParcela.BAIXADO) {
            throw new BusinessException("Esta conta já foi paga");
        }

        LocalDate dataEfetiva = dataBaixa != null ? dataBaixa : LocalDate.now();
        String descricao = "%s - %s".formatted(conta.getDescricao(), conta.getFornecedor().getNome());

        Transacao transacao = helper.gerarTransacaoGasto(
                conta.getFazenda(), conta.getNegocio(), conta.getSafra(), conta.getValor(), dataEfetiva, descricao);

        conta.setTransacao(transacao);
        conta.setStatusPagamento(StatusParcela.BAIXADO);
        conta.setDataBaixa(dataEfetiva);

        return ContaFornecedorResponse.de(contaFornecedorRepository.save(conta));
    }

    @Override
    public List<ContaFornecedorResponse> listarPorFornecedorEStatus(Long fornecedorId, StatusParcela status, String numeroUsuarioLogado) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado"));
        fazendaAcessoHelper.validarAcesso(fornecedor.getFazenda(), numeroUsuarioLogado);
        return contaFornecedorRepository.listarPendentesPorFornecedor(fornecedorId, status)
                .stream().map(ContaFornecedorResponse::de).toList();
    }
}