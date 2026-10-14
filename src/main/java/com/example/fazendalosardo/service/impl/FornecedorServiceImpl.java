package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.equipamento.FornecedorPagamentoRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Fornecedor;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.equipamento.FornecedorRepository;
import com.example.fazendalosardo.service.FornecedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;

    @Override
    public FornecedorResponse salvar(FornecedorRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(request.nome());
        fornecedor.setEspecialidade(request.especialidade());
        fornecedor.setObservacao(request.observacao());
        fornecedor.setFazenda(fazenda);
        fornecedor.setSaldoDevedor(BigDecimal.ZERO);
        fornecedor.setAtivo(true);
        return FornecedorResponse.de(fornecedorRepository.save(fornecedor));
    }

    @Override
    public Page<FornecedorResponse> listarPorFazenda(Long fazendaId, Pageable pageable, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return fornecedorRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId, pageable)
                .map(FornecedorResponse::de);
    }

    @Override
    public List<FornecedorResponse> listarTodosPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return fornecedorRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId)
                .stream().map(FornecedorResponse::de).toList();
    }

    @Override
    public Fornecedor acumularDivida(Long fornecedorId, BigDecimal valor, String numeroUsuarioLogado) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado"));
        fazendaAcessoHelper.validarAcesso(fornecedor.getFazenda(), numeroUsuarioLogado);
        fornecedor.setSaldoDevedor(fornecedor.getSaldoDevedor().add(valor));
        return fornecedorRepository.save(fornecedor);
    }

    @Override
    public FornecedorResponse registrarPagamento(Long fornecedorId, FornecedorPagamentoRequest request, String numeroUsuarioLogado) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado"));
        Fazenda fazenda = fornecedor.getFazenda();
        fazendaAcessoHelper.validarAcesso(fazenda, numeroUsuarioLogado);
        helper.validarVinculoXor(request.negocioId(), request.safraId());

        if (request.valor().compareTo(fornecedor.getSaldoDevedor()) > 0) {
            throw new BusinessException("O valor do pagamento não pode ser maior que o saldo devedor do fornecedor");
        }

        Negocio negocio = helper.buscarNegocio(request.negocioId());
        Safra safra = helper.buscarSafra(request.safraId());
        String descricao = "Pagamento fornecedor - %s".formatted(fornecedor.getNome());
        helper.gerarTransacaoGasto(fazenda, negocio, safra, request.valor(), request.data(), descricao);

        fornecedor.setSaldoDevedor(fornecedor.getSaldoDevedor().subtract(request.valor()));
        return FornecedorResponse.de(fornecedorRepository.save(fornecedor));
    }
}