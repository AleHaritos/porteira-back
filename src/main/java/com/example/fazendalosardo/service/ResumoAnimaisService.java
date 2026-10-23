package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.animais.ResumoAnimaisResponse;

import java.time.LocalDate;

public interface ResumoAnimaisService {
    ResumoAnimaisResponse buscarResumo(Long fazendaId, LocalDate dataInicio, LocalDate dataFim, String numeroUsuarioLogado);
}