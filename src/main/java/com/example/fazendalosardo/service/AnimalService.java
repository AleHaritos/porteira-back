package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.animais.*;
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

    void transferirAnimais(TransferenciaAnimalRequest request, String numeroUsuarioLogado);

    OperacaoVendaResponse venderAnimais(VendaAnimalRequest request, String numeroUsuarioLogado);

    List<OperacaoVendaResponse> listarOperacoesVenda(Long fazendaId, String numeroUsuarioLogado);

    AnimalCustoResponse calcularCusto(Long animalId, String numeroUsuarioLogado);

    List<HistoricoCuidadoAnimalResponse> listarHistorico(Long animalId, String numeroUsuarioLogado);
}