package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.CuidadoAnimalRequest;
import com.example.fazendalosardo.dto.animais.CuidadoAnimalResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.TipoCuidado;
import com.example.fazendalosardo.repository.animais.*;
import com.example.fazendalosardo.service.CuidadoAnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuidadoAnimalServiceImpl implements CuidadoAnimalService {

    private final CuidadoAnimalRepository cuidadoAnimalRepository;
    private final CuidadoAnimalRateioRepository cuidadoAnimalRateioRepository;
    private final AnimalRepository animalRepository;
    private final LoteRepository loteRepository;
    private final PiqueteRepository piqueteRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final RacaoRepository racaoRepository;

    @Override
    @Transactional
    public CuidadoAnimalResponse salvar(CuidadoAnimalRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Piquete piquete = piqueteRepository.findById(request.piqueteId())
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));

        Animal animal = null;
        Lote lote = null;

        switch (request.vinculoTipo()) {
            case ANIMAL -> {
                if (request.animalId() == null) {
                    throw new BusinessException("Selecione o animal para esse vínculo");
                }
                animal = animalRepository.findById(request.animalId())
                        .orElseThrow(() -> new NotFoundException("Animal não encontrado"));
            }
            case LOTE -> {
                if (request.loteId() == null) {
                    throw new BusinessException("Selecione o lote para esse vínculo");
                }
                lote = loteRepository.findById(request.loteId())
                        .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
            }
            case PIQUETE -> {

            }
        }

        CuidadoAnimal cuidado = new CuidadoAnimal();
        cuidado.setTipo(request.tipo());
        cuidado.setVinculoTipo(request.vinculoTipo());
        cuidado.setDescricao(request.descricao());
        cuidado.setCusto(request.custo());
        cuidado.setData(request.data());
        cuidado.setAnimal(animal);
        cuidado.setLote(lote);
        cuidado.setPiquete(piquete);
        cuidado.setFazenda(fazenda);

        Racao racao = null;
        if (request.racaoId() != null) {
            racao = racaoRepository.findById(request.racaoId())
                    .orElseThrow(() -> new NotFoundException("Ração não encontrada"));
        }
        cuidado.setRacao(racao);
        cuidado.setQuantidadeRacao(request.quantidadeRacao());

        cuidadoAnimalRepository.save(cuidado);

        gerarRateio(cuidado, animal, lote, piquete);

        return toResponse(cuidado);
    }

    private void gerarRateio(CuidadoAnimal cuidado, Animal animal, Lote lote, Piquete piquete) {
        List<Animal> animaisElegiveis = switch (cuidado.getVinculoTipo()) {
            case ANIMAL -> List.of(animal);
            case LOTE -> animalRepository.findByLoteIdAndPiqueteIdAndAtivoTrue(lote.getId(), piquete.getId());
            case PIQUETE -> animalRepository.findByPiqueteIdAndAtivoTrue(piquete.getId());
        };

        if (animaisElegiveis.isEmpty()) {
            throw new BusinessException("Não há animais ativos para ratear esse custo");
        }

        BigDecimal custoPorAnimal = cuidado.getCusto()
                .divide(BigDecimal.valueOf(animaisElegiveis.size()), 2, RoundingMode.HALF_UP);

        List<CuidadoAnimalRateio> rateios = animaisElegiveis.stream().map(a -> {
            CuidadoAnimalRateio r = new CuidadoAnimalRateio();
            r.setCuidadoAnimal(cuidado);
            r.setAnimal(a);
            r.setCustoAtribuido(custoPorAnimal);
            return r;
        }).toList();

        cuidadoAnimalRateioRepository.saveAll(rateios);
    }

    @Override
    public Page<CuidadoAnimalResponse> listarPorPiquete(Long piqueteId, TipoCuidado tipo, LocalDate dataInicio, LocalDate dataFim,
                                                        int page, int size, String numeroUsuarioLogado) {
        Piquete piquete = piqueteRepository.findById(piqueteId)
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));
        fazendaAcessoHelper.validarAcesso(piquete.getFazenda(), numeroUsuarioLogado);

        Pageable pageable = PageRequest.of(page, size);
        return cuidadoAnimalRepository.buscarPorPiqueteTipoEPeriodo(piqueteId, tipo, dataInicio, dataFim, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional
    public void excluir(Long id, String numeroUsuarioLogado) {
        CuidadoAnimal cuidado = cuidadoAnimalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro não encontrado"));
        fazendaAcessoHelper.validarAcesso(cuidado.getFazenda(), numeroUsuarioLogado);
        cuidadoAnimalRateioRepository.deleteByCuidadoAnimalId(id);
        cuidadoAnimalRepository.delete(cuidado);
    }

    private CuidadoAnimalResponse toResponse(CuidadoAnimal cuidado) {
        return new CuidadoAnimalResponse(
                cuidado.getId(),
                cuidado.getTipo(),
                cuidado.getVinculoTipo(),
                cuidado.getDescricao(),
                cuidado.getCusto(),
                cuidado.getData(),
                cuidado.getAnimal() != null ? cuidado.getAnimal().getId() : null,
                cuidado.getAnimal() != null ? cuidado.getAnimal().getTipo().name() : null,
                cuidado.getAnimal() != null ? cuidado.getAnimal().getDescricao() : null,
                cuidado.getLote() != null ? cuidado.getLote().getId() : null,
                cuidado.getLote() != null ? cuidado.getLote().getNome() : null,
                cuidado.getPiquete().getId(),
                cuidado.getPiquete().getNome(),
                cuidado.getRacao() != null ? cuidado.getRacao().getId() : null,
                cuidado.getRacao() != null ? cuidado.getRacao().getNome() : null,
                cuidado.getQuantidadeRacao()
        );
    }
}