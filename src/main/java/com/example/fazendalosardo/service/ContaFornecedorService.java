package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.equipamento.ContaFornecedorRequest;
import com.example.fazendalosardo.dto.equipamento.ContaFornecedorResponse;

import java.time.LocalDate;
import java.util.List;

public interface ContaFornecedorService {
    ContaFornecedorResponse salvar(ContaFornecedorRequest request, String numeroUsuarioLogado);
    List<ContaFornecedorResponse> listarPendentesPorFornecedor(Long fornecedorId, String numeroUsuarioLogado);
    ContaFornecedorResponse darBaixa(Long contaId, Long negocioId, Long safraId, LocalDate dataBaixa, String numeroUsuarioLogado);
}