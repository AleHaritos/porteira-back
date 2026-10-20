package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.safraDTO.CustosCombustivelManutencaoResponse;
import com.example.fazendalosardo.repository.equipamento.ManutencaoEquipamentoRepository;
import com.example.fazendalosardo.repository.veiculos.AbastecimentoRepository;
import com.example.fazendalosardo.repository.veiculos.ManutencaoRepository;
import com.example.fazendalosardo.service.CustoCategoriaSafraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustoCategoriaSafraServiceImpl implements CustoCategoriaSafraService {

    private final AbastecimentoRepository abastecimentoRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final ManutencaoEquipamentoRepository manutencaoEquipamentoRepository;

    @Override
    @Transactional(readOnly = true)
    public CustosCombustivelManutencaoResponse buscarPorSafra(Long safraId) {
        BigDecimal totalCombustivel = abastecimentoRepository.somarValorPorSafra(safraId);

        BigDecimal totalManutencao = manutencaoRepository.somarValorPorSafra(safraId)
                .add(manutencaoEquipamentoRepository.somarValorPorSafra(safraId));

        return new CustosCombustivelManutencaoResponse(totalCombustivel, totalManutencao);
    }
}