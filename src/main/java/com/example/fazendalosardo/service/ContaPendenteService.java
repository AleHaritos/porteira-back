package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.financeiro.ContaPendenteRequest;
import com.example.fazendalosardo.dto.financeiro.ContaPendenteResponse;
import com.example.fazendalosardo.dto.financeiro.ParcelaContaPendenteResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.model.enums.TipoTransacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ContaPendenteService {
    ContaPendenteResponse salvar(ContaPendenteRequest request, String numeroUsuarioLogado);

    Page<ParcelaContaPendenteResponse> buscarParcelasPorFazenda(Long fazendaId, StatusParcela status, TipoTransacao tipo,
                                                                Pageable pageable, String numeroUsuarioLogado);

    ParcelaContaPendenteResponse darBaixa(Long parcelaId, LocalDate dataBaixa, String numeroUsuarioLogado);
}
