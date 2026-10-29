package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.calendario.EventoCalendarioRequest;
import com.example.fazendalosardo.dto.calendario.EventoCalendarioResponse;

import java.time.LocalDate;
import java.util.List;

public interface EventoCalendarioService {
    EventoCalendarioResponse salvar(EventoCalendarioRequest request, String numeroUsuarioLogado);
    EventoCalendarioResponse atualizar(Long id, EventoCalendarioRequest request, String numeroUsuarioLogado);
    void excluir(Long id, String numeroUsuarioLogado);
    List<EventoCalendarioResponse> buscarPorFazendaEPeriodo(Long fazendaId, LocalDate dataInicio, LocalDate dataFim, String numeroUsuarioLogado);
}