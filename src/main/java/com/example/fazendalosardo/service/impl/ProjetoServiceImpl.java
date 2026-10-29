package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.projeto.ProjetoRequest;
import com.example.fazendalosardo.dto.projeto.ProjetoResponse;
import com.example.fazendalosardo.dto.projeto.ProjetoUpdateRequest;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.mapper.ProjetoMapper;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Projeto;
import com.example.fazendalosardo.repository.FazendaColaboradorRepository;
import com.example.fazendalosardo.repository.FazendaRepository;
import com.example.fazendalosardo.repository.ProjetoRepository;
import com.example.fazendalosardo.service.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoServiceImpl implements ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final FazendaRepository fazendaRepository;
    private final FazendaColaboradorRepository fazendaColaboradorRepository;
    private final ProjetoMapper projetoMapper;

    @Override
    @Transactional
    public ProjetoResponse salvar(ProjetoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaRepository.findById(request.fazendaId())
                .orElseThrow(() -> new NotFoundException("Fazenda não encontrada"));

        validarAcesso(fazenda, numeroUsuarioLogado);

        Projeto projeto = projetoMapper.toEntity(request);
        projeto.setFazenda(fazenda);
        projeto.setAtivo(true);

        return projetoMapper.toResponse(projetoRepository.save(projeto));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjetoResponse> buscarPorFazenda(Long fazendaId, Pageable pageable) {
        return projetoRepository.findByFazendaIdAndAtivoTrue(fazendaId, pageable)
                .map(projetoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjetoResponse> listarTodosPorFazenda(Long fazendaId) {
        return projetoRepository.findByFazendaIdAndAtivoTrueOrderByNomeAsc(fazendaId)
                .stream()
                .map(projetoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProjetoResponse atualizar(Long id, ProjetoUpdateRequest request, String numeroUsuarioLogado) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Projeto não encontrado"));

        validarAcesso(projeto.getFazenda(), numeroUsuarioLogado);

        projetoMapper.atualizar(request, projeto);
        return projetoMapper.toResponse(projetoRepository.save(projeto));
    }

    @Override
    @Transactional
    public void desativar(Long id, String numeroUsuarioLogado) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Projeto não encontrado"));

        validarAcesso(projeto.getFazenda(), numeroUsuarioLogado);

        projeto.setAtivo(false);
        projetoRepository.save(projeto);
    }

    @Override
    @Transactional
    public void reativar(Long id, String numeroUsuarioLogado) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Projeto não encontrado"));

        validarAcesso(projeto.getFazenda(), numeroUsuarioLogado);

        projeto.setAtivo(true);
        projetoRepository.save(projeto);
    }

    private void validarAcesso(Fazenda fazenda, String numeroUsuarioLogado) {
        boolean ehDono = fazenda.getDono().getNumero().equals(numeroUsuarioLogado);
        boolean ehColaborador = fazendaColaboradorRepository
                .existsByFazendaIdAndColaboradorNumero(fazenda.getId(), numeroUsuarioLogado);

        if (!ehDono && !ehColaborador) {
            throw new BusinessException("Você não tem permissão para gerenciar projetos dessa fazenda");
        }
    }
}