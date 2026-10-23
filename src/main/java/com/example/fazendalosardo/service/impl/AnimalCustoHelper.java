package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.AnimalCustoResponse;
import com.example.fazendalosardo.dto.animais.HistoricoCuidadoAnimalResponse;
import com.example.fazendalosardo.model.Animal;
import com.example.fazendalosardo.model.CuidadoAnimal;
import com.example.fazendalosardo.model.CuidadoAnimalRateio;
import com.example.fazendalosardo.model.enums.TipoCuidado;
import com.example.fazendalosardo.repository.animais.CuidadoAnimalRateioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Calcula o custo (vacinação + alimentação) atribuído a um animal específico,
 * lendo diretamente das linhas de rateio gravadas no momento em que cada
 * cuidado foi lançado (CuidadoAnimalRateio). Não recalcula nada ao vivo —
 * o que foi dividido na hora do registro é o que vale para sempre.
 */
@Component
@RequiredArgsConstructor
public class AnimalCustoHelper {

    private final CuidadoAnimalRateioRepository cuidadoAnimalRateioRepository;

    public AnimalCustoResponse calcularCusto(Animal animal) {
        BigDecimal vacinacao = BigDecimal.ZERO;
        BigDecimal alimentacao = BigDecimal.ZERO;

        for (CuidadoAnimalRateio r : cuidadoAnimalRateioRepository.buscarPorAnimal(animal.getId())) {
            CuidadoAnimal c = r.getCuidadoAnimal();
            if (c.getTipo() == TipoCuidado.VACINACAO) vacinacao = vacinacao.add(r.getCustoAtribuido());
            else alimentacao = alimentacao.add(r.getCustoAtribuido());
        }

        BigDecimal valorCompra = animal.getValorCompra() != null ? animal.getValorCompra() : BigDecimal.ZERO;
        BigDecimal total = vacinacao.add(alimentacao).add(valorCompra);

        return new AnimalCustoResponse(vacinacao, alimentacao, valorCompra, total);
    }

    public List<HistoricoCuidadoAnimalResponse> listarHistorico(Animal animal) {
        List<HistoricoCuidadoAnimalResponse> historico = new ArrayList<>();

        for (CuidadoAnimalRateio r : cuidadoAnimalRateioRepository.buscarPorAnimal(animal.getId())) {
            CuidadoAnimal c = r.getCuidadoAnimal();
            historico.add(new HistoricoCuidadoAnimalResponse(
                    c.getId(), c.getTipo(), origem(c), c.getDescricao(), c.getData(), c.getCusto(), r.getCustoAtribuido()
            ));
        }

        historico.sort(Comparator.comparing(HistoricoCuidadoAnimalResponse::data).reversed());
        return historico;
    }

    private String origem(CuidadoAnimal c) {
        return switch (c.getVinculoTipo()) {
            case ANIMAL -> "Direto no animal";
            case LOTE -> "Rateado do lote " + (c.getLote() != null ? c.getLote().getNome() : "");
            case PIQUETE -> "Rateado do piquete " + (c.getPiquete() != null ? c.getPiquete().getNome() : "");
        };
    }
}