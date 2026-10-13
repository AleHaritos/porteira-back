package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.veiculos.PostoPagamentoRequest;
import com.example.fazendalosardo.dto.veiculos.PostoRequest;
import com.example.fazendalosardo.dto.veiculos.PostoResponse;

import java.util.List;

public interface PostoService {
    PostoResponse salvar(PostoRequest request, String numeroUsuarioLogado);
    List<PostoResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado);
    PostoResponse registrarPagamento(Long postoId, PostoPagamentoRequest request, String numeroUsuarioLogado);
}
