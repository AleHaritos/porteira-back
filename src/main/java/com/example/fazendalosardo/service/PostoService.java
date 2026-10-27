package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.veiculos.PostoRequest;
import com.example.fazendalosardo.dto.veiculos.PostoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostoService {
    PostoResponse salvar(PostoRequest request, String numeroUsuarioLogado);
    Page<PostoResponse> listarPorFazenda(Long fazendaId, Pageable pageable, String numeroUsuarioLogado);
    List<PostoResponse> listarTodosPorFazenda(Long fazendaId, String numeroUsuarioLogado);
    void excluir(Long postoId, String numeroUsuarioLogado);
}