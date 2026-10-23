package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.AnimalCustoResponse;
import com.example.fazendalosardo.dto.animais.HistoricoCuidadoAnimalResponse;
import com.example.fazendalosardo.model.Animal;
import com.example.fazendalosardo.model.CuidadoAnimal;
import com.example.fazendalosardo.model.enums.TipoCuidado;
import com.example.fazendalosardo.model.enums.TipoVinculoCuidado;
import com.example.fazendalosardo.repository.animais.AnimalRepository;
import com.example.fazendalosardo.repository.animais.CuidadoAnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Calcula o custo (vacinação + alimentação) atribuído a um animal específico,
 * somando:
 *  1) registros lançados diretamente no animal;
 *  2) a parte proporcional de registros lançados no lote do animal, dividida
 *     pela quantidade de animais ativos daquele lote;
 *  3) a parte proporcional de registros lançados no piquete do animal, dividida
 *     pela quantidade de animais ativos daquele piquete.
 */
@Component
@RequiredArgsConstructor
public class AnimalCustoHelper {

    private final CuidadoAnimalRepository cuidadoAnimalRepository;
    private final AnimalRepository animalRepository;

    public AnimalCustoResponse calcularCusto(Animal animal) {
        BigDecimal vacinacao = BigDecimal.ZERO;
        BigDecimal alimentacao = BigDecimal.ZERO;

        for (CuidadoAnimal c : cuidadoAnimalRepository.findByAnimalIdAndVinculoTipo(animal.getId(), TipoVinculoCuidado.ANIMAL)) {
            if (c.getTipo() == TipoCuidado.VACINACAO) vacinacao = vacinacao.add(c.getCusto());
            else alimentacao = alimentacao.add(c.getCusto());
        }

        if (animal.getLote() != null) {
            long qtdNoLote = animalRepository.countByLoteIdAndAtivoTrue(animal.getLote().getId());
            if (qtdNoLote > 0) {
                for (CuidadoAnimal c : cuidadoAnimalRepository.findByLoteIdAndVinculoTipo(animal.getLote().getId(), TipoVinculoCuidado.LOTE)) {
                    BigDecimal parte = c.getCusto().divide(BigDecimal.valueOf(qtdNoLote), 2, RoundingMode.HALF_UP);
                    if (c.getTipo() == TipoCuidado.VACINACAO) vacinacao = vacinacao.add(parte);
                    else alimentacao = alimentacao.add(parte);
                }
            }
        }

        long qtdNoPiquete = animalRepository.countByPiqueteIdAndAtivoTrue(animal.getPiquete().getId());
        if (qtdNoPiquete > 0) {
            for (CuidadoAnimal c : cuidadoAnimalRepository.findByPiqueteIdAndVinculoTipo(animal.getPiquete().getId(), TipoVinculoCuidado.PIQUETE)) {
                BigDecimal parte = c.getCusto().divide(BigDecimal.valueOf(qtdNoPiquete), 2, RoundingMode.HALF_UP);
                if (c.getTipo() == TipoCuidado.VACINACAO) vacinacao = vacinacao.add(parte);
                else alimentacao = alimentacao.add(parte);
            }
        }

        BigDecimal valorCompra = animal.getValorCompra() != null ? animal.getValorCompra() : BigDecimal.ZERO;
        BigDecimal total = vacinacao.add(alimentacao).add(valorCompra);

        return new AnimalCustoResponse(vacinacao, alimentacao, valorCompra, total);
    }

    public List<HistoricoCuidadoAnimalResponse> listarHistorico(Animal animal) {
        List<HistoricoCuidadoAnimalResponse> historico = new ArrayList<>();

        for (CuidadoAnimal c : cuidadoAnimalRepository.findByAnimalIdAndVinculoTipo(animal.getId(), TipoVinculoCuidado.ANIMAL)) {
            historico.add(new HistoricoCuidadoAnimalResponse(
                    c.getId(), c.getTipo(), "Direto no animal", c.getDescricao(), c.getData(), c.getCusto(), c.getCusto()
            ));
        }

        if (animal.getLote() != null) {
            long qtdNoLote = animalRepository.countByLoteIdAndAtivoTrue(animal.getLote().getId());
            if (qtdNoLote > 0) {
                for (CuidadoAnimal c : cuidadoAnimalRepository.findByLoteIdAndVinculoTipo(animal.getLote().getId(), TipoVinculoCuidado.LOTE)) {
                    BigDecimal parte = c.getCusto().divide(BigDecimal.valueOf(qtdNoLote), 2, RoundingMode.HALF_UP);
                    historico.add(new HistoricoCuidadoAnimalResponse(
                            c.getId(), c.getTipo(), "Rateado do lote " + animal.getLote().getNome(), c.getDescricao(), c.getData(), c.getCusto(), parte
                    ));
                }
            }
        }

        long qtdNoPiquete = animalRepository.countByPiqueteIdAndAtivoTrue(animal.getPiquete().getId());
        if (qtdNoPiquete > 0) {
            for (CuidadoAnimal c : cuidadoAnimalRepository.findByPiqueteIdAndVinculoTipo(animal.getPiquete().getId(), TipoVinculoCuidado.PIQUETE)) {
                BigDecimal parte = c.getCusto().divide(BigDecimal.valueOf(qtdNoPiquete), 2, RoundingMode.HALF_UP);
                historico.add(new HistoricoCuidadoAnimalResponse(
                        c.getId(), c.getTipo(), "Rateado do piquete " + animal.getPiquete().getNome(), c.getDescricao(), c.getData(), c.getCusto(), parte
                ));
            }
        }

        historico.sort(Comparator.comparing(HistoricoCuidadoAnimalResponse::data).reversed());
        return historico;
    }
}