package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.equipamento.FornecedorPagamentoRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorResponse;
import com.example.fazendalosardo.model.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface FornecedorService {
    FornecedorResponse salvar(FornecedorRequest request, String numeroUsuarioLogado);
    Page<FornecedorResponse> listarPorFazenda(Long fazendaId, Pageable pageable, String numeroUsuarioLogado);
    List<FornecedorResponse> listarTodosPorFazenda(Long fazendaId, String numeroUsuarioLogado);
    FornecedorResponse buscarPorId(Long id, String numeroUsuarioLogado);
    FornecedorResponse registrarPagamento(Long fornecedorId, FornecedorPagamentoRequest request, String numeroUsuarioLogado);
    Fornecedor acumularDivida(Long fornecedorId, BigDecimal valor, String numeroUsuarioLogado);
    Fornecedor baixarDivida(Long fornecedorId, BigDecimal valor, String numeroUsuarioLogado);
}