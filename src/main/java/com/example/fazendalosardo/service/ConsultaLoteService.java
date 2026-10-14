package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.animais.ConsultaLoteRequest;
import com.example.fazendalosardo.dto.animais.ConsultaLoteResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ConsultaLoteService {
    ConsultaLoteResponse salvar(ConsultaLoteRequest request, String numeroUsuarioLogado);
    Page<ConsultaLoteResponse> buscarPorLote(Long loteId, int page, int size, String numeroUsuarioLogado);
}