package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.equipamento.FornecedorDebitoResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fornecedor;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.repository.equipamento.FornecedorDebitoRepository;
import com.example.fazendalosardo.repository.equipamento.FornecedorRepository;
import com.example.fazendalosardo.service.FornecedorDebitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FornecedorDebitoServiceImpl implements FornecedorDebitoService {

    private final FornecedorDebitoRepository fornecedorDebitoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    public Page<FornecedorDebitoResponse> listarPorFornecedorEStatus(Long fornecedorId, StatusParcela status, LocalDate dataInicio, LocalDate dataFim, Pageable pageable, String numeroUsuarioLogado) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado"));
        fazendaAcessoHelper.validarAcesso(fornecedor.getFazenda(), numeroUsuarioLogado);

        return fornecedorDebitoRepository.buscarPorFornecedorEStatus(fornecedorId, status.name(), dataInicio, dataFim, pageable)
                .map(FornecedorDebitoResponse::de);
    }
}