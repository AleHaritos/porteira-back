package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.animais.CuidadoAnimalRequest;
import com.example.fazendalosardo.dto.animais.CuidadoAnimalResponse;
import com.example.fazendalosardo.model.enums.TipoCuidado;

import java.util.List;

public interface CuidadoAnimalService {

    CuidadoAnimalResponse salvar(CuidadoAnimalRequest request, String numeroUsuarioLogado);

    List<CuidadoAnimalResponse> listarPorPiquete(Long piqueteId, TipoCuidado tipo, String numeroUsuarioLogado);

    void excluir(Long id, String numeroUsuarioLogado);
}