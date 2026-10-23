package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.IngredienteRequest;
import com.example.fazendalosardo.dto.animais.IngredienteResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Ingrediente;
import com.example.fazendalosardo.repository.animais.IngredienteRepository;
import com.example.fazendalosardo.service.IngredienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredienteServiceImpl implements IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    @Transactional
    public IngredienteResponse salvar(IngredienteRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNome(request.nome());
        ingrediente.setValorUnitario(request.valorUnitario());
        ingrediente.setUnidade(request.unidade());
        ingrediente.setFazenda(fazenda);

        return toResponse(ingredienteRepository.save(ingrediente));
    }

    @Override
    @Transactional
    public IngredienteResponse atualizar(Long id, IngredienteRequest request, String numeroUsuarioLogado) {
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ingrediente não encontrado"));
        fazendaAcessoHelper.validarAcesso(ingrediente.getFazenda(), numeroUsuarioLogado);

        ingrediente.setNome(request.nome());
        ingrediente.setValorUnitario(request.valorUnitario());
        ingrediente.setUnidade(request.unidade());

        return toResponse(ingrediente);
    }

    @Override
    public List<IngredienteResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return ingredienteRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void inativar(Long id, String numeroUsuarioLogado) {
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ingrediente não encontrado"));
        fazendaAcessoHelper.validarAcesso(ingrediente.getFazenda(), numeroUsuarioLogado);
        ingrediente.setAtivo(false);
    }

    private IngredienteResponse toResponse(Ingrediente ingrediente) {
        return new IngredienteResponse(
                ingrediente.getId(), ingrediente.getNome(), ingrediente.getValorUnitario(),
                ingrediente.getUnidade(), ingrediente.getAtivo()
        );
    }
}