package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.animais.AnimalEmMassaRequest;
import com.example.fazendalosardo.dto.animais.AnimalRequest;
import com.example.fazendalosardo.dto.animais.AnimalResponse;
import com.example.fazendalosardo.dto.animais.VendaAnimalRequest;
import com.example.fazendalosardo.model.enums.TipoAnimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnimalService {
    List<AnimalResponse> salvarEmMassa(AnimalEmMassaRequest request, String numeroUsuarioLogado);
    AnimalResponse atualizar(Long id, AnimalRequest request, String numeroUsuarioLogado);
    Page<AnimalResponse> listarPorFazenda(Long fazendaId, Long piqueteId, Long loteId, TipoAnimal tipo,
                                          Pageable pageable, String numeroUsuarioLogado);
    void excluir(Long id, String numeroUsuarioLogado);
    void venderAnimais(VendaAnimalRequest request, String numeroUsuarioLogado);
}