package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.equipamento.ContaFornecedorRequest;
import com.example.fazendalosardo.dto.equipamento.ContaFornecedorResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;

import java.time.LocalDate;
import java.util.List;

public interface ContaFornecedorService {
    ContaFornecedorResponse salvar(ContaFornecedorRequest request, String numeroUsuarioLogado);
    List<ContaFornecedorResponse> listarPendentesPorFornecedor(Long fornecedorId, String numeroUsuarioLogado);
    ContaFornecedorResponse darBaixa(Long contaId, LocalDate dataBaixa, String numeroUsuarioLogado);
    List<ContaFornecedorResponse> listarPorFornecedorEStatus(Long fornecedorId, StatusParcela status, String numeroUsuarioLogado);
}