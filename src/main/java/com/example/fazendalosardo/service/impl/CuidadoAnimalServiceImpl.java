package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.CuidadoAnimalRequest;
import com.example.fazendalosardo.dto.animais.CuidadoAnimalResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.TipoCuidado;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.animais.AnimalRepository;
import com.example.fazendalosardo.repository.animais.CuidadoAnimalRepository;
import com.example.fazendalosardo.repository.animais.LoteRepository;
import com.example.fazendalosardo.repository.animais.PiqueteRepository;
import com.example.fazendalosardo.service.CuidadoAnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuidadoAnimalServiceImpl implements CuidadoAnimalService {

    private final CuidadoAnimalRepository cuidadoAnimalRepository;
    private final AnimalRepository animalRepository;
    private final LoteRepository loteRepository;
    private final PiqueteRepository piqueteRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper transacaoHelper;

    @Override
    @Transactional
    public CuidadoAnimalResponse salvar(CuidadoAnimalRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Piquete piquete = piqueteRepository.findById(request.piqueteId())
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));

        transacaoHelper.validarVinculoXor(request.negocioId(), request.safraId());
        Negocio negocio = transacaoHelper.buscarNegocio(request.negocioId());
        Safra safra = transacaoHelper.buscarSafra(request.safraId());

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
                // aplica a todo o piquete, sem animal/lote específico
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
        cuidado.setNegocio(negocio);
        cuidado.setSafra(safra);

        cuidadoAnimalRepository.save(cuidado);

        String prefixo = request.tipo() == TipoCuidado.VACINACAO ? "Vacinação" : "Alimentação";
        String descricaoTransacao = prefixo
                + (request.descricao() != null && !request.descricao().isBlank() ? " - " + request.descricao() : "")
                + " - " + piquete.getNome();

        transacaoHelper.gerarTransacaoGasto(fazenda, negocio, safra, request.custo(), request.data(), descricaoTransacao);

        return toResponse(cuidado);
    }

    @Override
    public List<CuidadoAnimalResponse> listarPorPiquete(Long piqueteId, TipoCuidado tipo, String numeroUsuarioLogado) {
        Piquete piquete = piqueteRepository.findById(piqueteId)
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));
        fazendaAcessoHelper.validarAcesso(piquete.getFazenda(), numeroUsuarioLogado);

        return cuidadoAnimalRepository.buscarPorPiqueteETipo(piqueteId, tipo).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void excluir(Long id, String numeroUsuarioLogado) {
        CuidadoAnimal cuidado = cuidadoAnimalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro não encontrado"));
        fazendaAcessoHelper.validarAcesso(cuidado.getFazenda(), numeroUsuarioLogado);
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
                cuidado.getNegocio() != null ? cuidado.getNegocio().getId() : null,
                cuidado.getNegocio() != null ? cuidado.getNegocio().getNome() : null,
                cuidado.getSafra() != null ? cuidado.getSafra().getId() : null,
                cuidado.getSafra() != null ? cuidado.getSafra().getNome() : null
        );
    }
}