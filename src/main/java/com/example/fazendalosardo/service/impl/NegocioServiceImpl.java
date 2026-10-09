package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.negocio.NegocioRequest;
import com.example.fazendalosardo.dto.negocio.NegocioResponse;
import com.example.fazendalosardo.dto.negocio.NegocioUpdateRequest;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.mapper.NegocioMapper;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.repository.FazendaColaboradorRepository;
import com.example.fazendalosardo.repository.FazendaRepository;
import com.example.fazendalosardo.repository.NegocioRepository;
import com.example.fazendalosardo.service.NegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NegocioServiceImpl implements NegocioService {

    private final NegocioRepository negocioRepository;
    private final FazendaRepository fazendaRepository;
    private final FazendaColaboradorRepository fazendaColaboradorRepository;
    private final NegocioMapper negocioMapper;

    @Override
    @Transactional
    public NegocioResponse salvar(NegocioRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaRepository.findById(request.fazendaId())
                .orElseThrow(() -> new NotFoundException("Fazenda não encontrada"));

        validarAcesso(fazenda, numeroUsuarioLogado);

        Negocio negocio = negocioMapper.toEntity(request);
        negocio.setFazenda(fazenda);
        negocio.setAtivo(true);

        return negocioMapper.toResponse(negocioRepository.save(negocio));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NegocioResponse> buscarPorFazenda(Long fazendaId, Pageable pageable) {
        return negocioRepository.findByFazendaIdAndAtivoTrue(fazendaId, pageable)
                .map(negocioMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NegocioResponse> listarTodosPorFazenda(Long fazendaId) {
        return negocioRepository.findByFazendaIdAndAtivoTrueOrderByNomeAsc(fazendaId)
                .stream()
                .map(negocioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public NegocioResponse atualizar(Long id, NegocioUpdateRequest request, String numeroUsuarioLogado) {
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Negócio não encontrado"));

        validarAcesso(negocio.getFazenda(), numeroUsuarioLogado);

        negocioMapper.atualizar(request, negocio);
        return negocioMapper.toResponse(negocioRepository.save(negocio));
    }

    @Override
    @Transactional
    public void desativar(Long id, String numeroUsuarioLogado) {
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Negócio não encontrado"));

        validarAcesso(negocio.getFazenda(), numeroUsuarioLogado);

        negocio.setAtivo(false);
        negocioRepository.save(negocio);
    }

    @Override
    @Transactional
    public void reativar(Long id, String numeroUsuarioLogado) {
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Negócio não encontrado"));

        validarAcesso(negocio.getFazenda(), numeroUsuarioLogado);

        negocio.setAtivo(true);
        negocioRepository.save(negocio);
    }

    private void validarAcesso(Fazenda fazenda, String numeroUsuarioLogado) {
        boolean ehDono = fazenda.getDono().getNumero().equals(numeroUsuarioLogado);
        boolean ehColaborador = fazendaColaboradorRepository
                .existsByFazendaIdAndColaboradorNumero(fazenda.getId(), numeroUsuarioLogado);

        if (!ehDono && !ehColaborador) {
            throw new BusinessException("Você não tem permissão para gerenciar negócios dessa fazenda");
        }
    }
}