package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.veiculos.ManutencaoParceladaRequest;
import com.example.fazendalosardo.dto.veiculos.ManutencaoParceladaResponse;
import com.example.fazendalosardo.dto.veiculos.ParcelaManutencaoResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ManutencaoParceladaService {
    ManutencaoParceladaResponse salvar(ManutencaoParceladaRequest request, String numeroUsuarioLogado);
    Page<ParcelaManutencaoResponse> buscarParcelasPorFazenda(Long fazendaId, StatusParcela status, Pageable pageable, String numeroUsuarioLogado);
    ParcelaManutencaoResponse darBaixa(Long parcelaId, LocalDate dataBaixa, String numeroUsuarioLogado);
}