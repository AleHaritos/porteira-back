package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.ResumoAnimaisResponse;
import com.example.fazendalosardo.model.CuidadoAnimal;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.OperacaoVenda;
import com.example.fazendalosardo.model.enums.TipoCuidado;
import com.example.fazendalosardo.repository.animais.CuidadoAnimalRateioRepository;
import com.example.fazendalosardo.repository.animais.CuidadoAnimalRepository;
import com.example.fazendalosardo.repository.animais.CustoPorTipoProjection;
import com.example.fazendalosardo.repository.animais.OperacaoVendaRepository;
import com.example.fazendalosardo.repository.animais.ResumoVendaProjection;
import com.example.fazendalosardo.service.ResumoAnimaisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumoAnimaisServiceImpl implements ResumoAnimaisService {

    private final CuidadoAnimalRateioRepository cuidadoAnimalRateioRepository;
    private final CuidadoAnimalRepository cuidadoAnimalRepository;
    private final OperacaoVendaRepository operacaoVendaRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    public ResumoAnimaisResponse buscarResumo(Long fazendaId, LocalDate dataInicio, LocalDate dataFim, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);

        List<CustoPorTipoProjection> custosPorTipo = cuidadoAnimalRateioRepository.somarCustoPorTipo(fazenda.getId(), dataInicio, dataFim);

        BigDecimal gastoAlimentacao = custosPorTipo.stream()
                .filter(c -> c.getTipo() == TipoCuidado.ALIMENTACAO)
                .map(CustoPorTipoProjection::getTotal)
                .findFirst()
                .orElse(BigDecimal.ZERO);

        BigDecimal gastoVacinacao = custosPorTipo.stream()
                .filter(c -> c.getTipo() == TipoCuidado.VACINACAO)
                .map(CustoPorTipoProjection::getTotal)
                .findFirst()
                .orElse(BigDecimal.ZERO);

        ResumoVendaProjection resumoVenda = operacaoVendaRepository.somarPorPeriodo(fazenda.getId(), dataInicio, dataFim);

        BigDecimal receitaVendas = resumoVenda.getReceitaTotal() != null ? resumoVenda.getReceitaTotal() : BigDecimal.ZERO;
        BigDecimal custoVendas = resumoVenda.getCustoTotal() != null ? resumoVenda.getCustoTotal() : BigDecimal.ZERO;
        BigDecimal lucroVendas = resumoVenda.getLucroTotal() != null ? resumoVenda.getLucroTotal() : BigDecimal.ZERO;

        List<OperacaoVenda> operacoes = operacaoVendaRepository.buscarPorFazendaEPeriodo(fazenda.getId(), dataInicio, dataFim);
        List<ResumoAnimaisResponse.OperacaoVendaResumoItem> operacoesResumo = operacoes.stream()
                .map(o -> new ResumoAnimaisResponse.OperacaoVendaResumoItem(
                        o.getId(),
                        o.getData(),
                        o.getDescricao(),
                        o.getValorVenda(),
                        o.getCustoTotal(),
                        o.getLucro(),
                        o.getItens() != null ? o.getItens().size() : 0
                ))
                .toList();

        List<CuidadoAnimal> cuidados = cuidadoAnimalRepository.buscarPorFazendaEPeriodo(fazenda.getId(), dataInicio, dataFim);
        List<ResumoAnimaisResponse.CuidadoAnimalResumoItem> cuidadosResumo = cuidados.stream()
                .map(c -> new ResumoAnimaisResponse.CuidadoAnimalResumoItem(
                        c.getId(),
                        c.getTipo().name(),
                        c.getData(),
                        c.getDescricao(),
                        c.getCusto(),
                        c.getPiquete() != null ? c.getPiquete().getNome() : null,
                        destino(c)
                ))
                .toList();

        return new ResumoAnimaisResponse(
                gastoAlimentacao,
                gastoVacinacao,
                gastoAlimentacao.add(gastoVacinacao),
                receitaVendas,
                custoVendas,
                lucroVendas,
                operacoesResumo,
                cuidadosResumo
        );
    }

    private String destino(CuidadoAnimal c) {
        return switch (c.getVinculoTipo()) {
            case ANIMAL -> c.getAnimal() != null ? c.getAnimal().getDescricao() : "Animal";
            case LOTE -> c.getLote() != null ? c.getLote().getNome() : "Lote";
            case PIQUETE -> "Todo o piquete";
        };
    }
}