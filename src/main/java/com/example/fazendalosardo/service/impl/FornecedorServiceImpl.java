package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.equipamento.FornecedorRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Fornecedor;
import com.example.fazendalosardo.repository.equipamento.FornecedorDebitoRepository;
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
    private final FornecedorDebitoRepository fornecedorDebitoRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    public FornecedorResponse salvar(FornecedorRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(request.nome());
        fornecedor.setEspecialidade(request.especialidade());
        fornecedor.setObservacao(request.observacao());
        fornecedor.setFazenda(fazenda);
        fornecedor.setAtivo(true);
        return FornecedorResponse.de(fornecedorRepository.save(fornecedor), BigDecimal.ZERO);
    }

    @Override
    public FornecedorResponse buscarPorId(Long id, String numeroUsuarioLogado) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado"));
        fazendaAcessoHelper.validarAcesso(fornecedor.getFazenda(), numeroUsuarioLogado);
        return FornecedorResponse.de(fornecedor, fornecedorDebitoRepository.somarPendentePorFornecedor(fornecedor.getId()));
    }

    @Override
    public Page<FornecedorResponse> listarPorFazenda(Long fazendaId, Pageable pageable, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return fornecedorRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId, pageable)
                .map(fornecedor -> FornecedorResponse.de(fornecedor, fornecedorDebitoRepository.somarPendentePorFornecedor(fornecedor.getId())));
    }

    @Override
    public List<FornecedorResponse> listarTodosPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return fornecedorRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId).stream()
                .map(fornecedor -> FornecedorResponse.de(fornecedor, fornecedorDebitoRepository.somarPendentePorFornecedor(fornecedor.getId())))
                .toList();
    }
}