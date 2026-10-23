package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.PiqueteRequest;
import com.example.fazendalosardo.dto.animais.PiqueteResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Piquete;
import com.example.fazendalosardo.repository.animais.AnimalRepository;
import com.example.fazendalosardo.repository.animais.PiqueteRepository;
import com.example.fazendalosardo.service.PiqueteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PiqueteServiceImpl implements PiqueteService {

    private final PiqueteRepository piqueteRepository;
    private final AnimalRepository animalRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    @Transactional
    public PiqueteResponse salvar(PiqueteRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Piquete piquete = new Piquete();
        piquete.setNome(request.nome());
        piquete.setAreaHectares(request.areaHectares());
        piquete.setFazenda(fazenda);
        piquete.setAtivo(true);

        return toResponse(piqueteRepository.save(piquete));
    }

    @Override
    @Transactional
    public PiqueteResponse atualizar(Long id, PiqueteRequest request, String numeroUsuarioLogado) {
        Piquete piquete = piqueteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));

        fazendaAcessoHelper.validarAcesso(piquete.getFazenda(), numeroUsuarioLogado);

        piquete.setNome(request.nome());
        piquete.setAreaHectares(request.areaHectares());

        return toResponse(piquete);
    }

    @Override
    public List<PiqueteResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return piqueteRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void inativar(Long id, String numeroUsuarioLogado) {
        Piquete piquete = piqueteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));
        fazendaAcessoHelper.validarAcesso(piquete.getFazenda(), numeroUsuarioLogado);
        piquete.setAtivo(false);
    }

    private PiqueteResponse toResponse(Piquete piquete) {
        long quantidadeAnimais = animalRepository.countByPiqueteIdAndAtivoTrue(piquete.getId());
        return new PiqueteResponse(
                piquete.getId(),
                piquete.getNome(),
                piquete.getAreaHectares(),
                piquete.getAtivo(),
                quantidadeAnimais
        );
    }
}