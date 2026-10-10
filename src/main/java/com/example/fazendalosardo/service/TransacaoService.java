package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.financeiro.ResumoFinanceiroResponse;
import com.example.fazendalosardo.dto.financeiro.TransacaoRequest;
import com.example.fazendalosardo.dto.financeiro.TransacaoResponse;
import com.example.fazendalosardo.dto.financeiro.TransacaoUpdateRequest;
import com.example.fazendalosardo.model.enums.TipoTransacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TransacaoService {
    TransacaoResponse salvar(TransacaoRequest request, String numeroUsuarioLogado);

    Page<TransacaoResponse> buscarPorFazenda(Long fazendaId, LocalDate dataInicio, LocalDate dataFim,
                                             TipoTransacao tipo, Pageable pageable, String numeroUsuarioLogado);

    TransacaoResponse atualizar(Long id, TransacaoUpdateRequest request, String numeroUsuarioLogado);
    void desativar(Long id, String numeroUsuarioLogado);
    void reativar(Long id, String numeroUsuarioLogado);
    ResumoFinanceiroResponse buscarResumo(Long fazendaId, LocalDate dataInicio, LocalDate dataFim, String numeroUsuarioLogado);
}