package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.animais.CuidadoAnimalRequest;
import com.example.fazendalosardo.dto.animais.CuidadoAnimalResponse;
import com.example.fazendalosardo.model.enums.TipoCuidado;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface CuidadoAnimalService {

    CuidadoAnimalResponse salvar(CuidadoAnimalRequest request, String numeroUsuarioLogado);

    Page<CuidadoAnimalResponse> listarPorPiquete(Long piqueteId, TipoCuidado tipo, LocalDate dataInicio, LocalDate dataFim,
                                                 int page, int size, String numeroUsuarioLogado);

    void excluir(Long id, String numeroUsuarioLogado);
}