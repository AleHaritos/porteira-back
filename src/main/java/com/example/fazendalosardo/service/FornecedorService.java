package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.equipamento.FornecedorPagamentoRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorResponse;
import com.example.fazendalosardo.model.Fornecedor;

import java.math.BigDecimal;
import java.util.List;

public interface FornecedorService {
    FornecedorResponse salvar(FornecedorRequest request, String numeroUsuarioLogado);
    List<FornecedorResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado);
    FornecedorResponse registrarPagamento(Long fornecedorId, FornecedorPagamentoRequest request, String numeroUsuarioLogado);
    Fornecedor acumularDivida(Long fornecedorId, BigDecimal valor, String numeroUsuarioLogado);
}