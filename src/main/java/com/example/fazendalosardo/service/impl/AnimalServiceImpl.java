package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.AnimalEmMassaRequest;
import com.example.fazendalosardo.dto.animais.AnimalRequest;
import com.example.fazendalosardo.dto.animais.AnimalResponse;
import com.example.fazendalosardo.dto.animais.VendaAnimalRequest;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Animal;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Lote;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.model.Piquete;
import com.example.fazendalosardo.model.enums.TipoAnimal;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.animais.AnimalRepository;
import com.example.fazendalosardo.repository.animais.LoteRepository;
import com.example.fazendalosardo.repository.animais.PiqueteRepository;
import com.example.fazendalosardo.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final LoteRepository loteRepository;
    private final PiqueteRepository piqueteRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper transacaoHelper;

    @Override
    @Transactional
    public List<AnimalResponse> salvarEmMassa(AnimalEmMassaRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Piquete piquete = piqueteRepository.findById(request.piqueteId())
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));

        List<Animal> animais = request.animais().stream().map(item -> {
            Lote lote = null;
            if (item.loteId() != null) {
                lote = loteRepository.findById(item.loteId())
                        .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
            }

            Animal animal = new Animal();
            animal.setTipo(item.tipo());
            animal.setLote(lote);
            animal.setPiquete(piquete);
            animal.setFazenda(fazenda);
            animal.setDescricao(item.descricao());
            return animal;
        }).toList();

        return animalRepository.saveAll(animais).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public AnimalResponse atualizar(Long id, AnimalRequest request, String numeroUsuarioLogado) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal não encontrado"));

        fazendaAcessoHelper.validarAcesso(animal.getFazenda(), numeroUsuarioLogado);

        Piquete piquete = piqueteRepository.findById(request.piqueteId())
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));

        Lote lote = null;
        if (request.loteId() != null) {
            lote = loteRepository.findById(request.loteId())
                    .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
        }

        animal.setTipo(request.tipo());
        animal.setLote(lote);
        animal.setPiquete(piquete);
        animal.setDescricao(request.descricao());

        return toResponse(animal);
    }

    @Override
    public Page<AnimalResponse> listarPorFazenda(Long fazendaId, Long piqueteId, Long loteId, TipoAnimal tipo,
                                                 Pageable pageable, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return animalRepository.buscarComFiltros(fazendaId, piqueteId, loteId, tipo, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional
    public void excluir(Long id, String numeroUsuarioLogado) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal não encontrado"));
        fazendaAcessoHelper.validarAcesso(animal.getFazenda(), numeroUsuarioLogado);
        animalRepository.delete(animal);
    }

    @Override
    @Transactional
    public void venderAnimais(VendaAnimalRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        transacaoHelper.validarVinculoXor(request.negocioId(), request.safraId());
        Negocio negocio = transacaoHelper.buscarNegocio(request.negocioId());
        Safra safra = transacaoHelper.buscarSafra(request.safraId());

        List<Animal> animais = animalRepository.findAllById(request.animalIds());

        if (animais.size() != request.animalIds().size()) {
            throw new NotFoundException("Um ou mais animais não foram encontrados");
        }

        boolean algumDeOutraFazenda = animais.stream()
                .anyMatch(a -> !a.getFazenda().getId().equals(fazenda.getId()));
        if (algumDeOutraFazenda) {
            throw new BusinessException("Todos os animais devem pertencer à fazenda informada");
        }

        String descricaoTransacao = "Venda de " + animais.size() + " animal(is)"
                + (request.descricao() != null && !request.descricao().isBlank() ? " - " + request.descricao() : "");

        transacaoHelper.gerarTransacaoReceita(fazenda, negocio, safra, request.valor(), request.data(), descricaoTransacao);

        animalRepository.deleteAll(animais);
    }

    private AnimalResponse toResponse(Animal animal) {
        return new AnimalResponse(
                animal.getId(),
                animal.getTipo(),
                animal.getLote() != null ? animal.getLote().getId() : null,
                animal.getLote() != null ? animal.getLote().getNome() : null,
                animal.getPiquete().getId(),
                animal.getPiquete().getNome(),
                animal.getDescricao()
        );
    }
}