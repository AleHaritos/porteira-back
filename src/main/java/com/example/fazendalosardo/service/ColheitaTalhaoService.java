package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.safraDTO.ColheitaTalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.ColheitaTalhaoResponse;

import java.util.List;

public interface ColheitaTalhaoService {
    ColheitaTalhaoResponse salvar(ColheitaTalhaoRequest request);
    List<ColheitaTalhaoResponse> listarPorTalhao(Long talhaoId);
    void excluir(Long id);
}
