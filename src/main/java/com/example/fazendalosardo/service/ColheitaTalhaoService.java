package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.safraDTO.ColheitaTalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.ColheitaTalhaoResponse;
import com.example.fazendalosardo.dto.safraDTO.ColheitaTalhaoUpdateRequest;

import java.util.List;

public interface ColheitaTalhaoService {
    ColheitaTalhaoResponse salvar(ColheitaTalhaoRequest request);
    ColheitaTalhaoResponse atualizar(Long id, ColheitaTalhaoUpdateRequest request);
    List<ColheitaTalhaoResponse> listarPorTalhao(Long talhaoId);
    void excluir(Long id);
}