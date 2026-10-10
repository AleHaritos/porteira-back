package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.financeiro.ResumoFinanceiroResponse;
import com.example.fazendalosardo.dto.financeiro.TransacaoRequest;
import com.example.fazendalosardo.dto.financeiro.TransacaoResponse;
import com.example.fazendalosardo.dto.financeiro.TransacaoUpdateRequest;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.mapper.TransacaoMapper;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.model.Transacao;
import com.example.fazendalosardo.model.enums.TipoTransacao;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.FazendaColaboradorRepository;
import com.example.fazendalosardo.repository.FazendaRepository;
import com.example.fazendalosardo.repository.NegocioRepository;
import com.example.fazendalosardo.repository.TransacaoRepository;
import com.example.fazendalosardo.repository.safras.SafraRepository;
import com.example.fazendalosardo.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransacaoServiceImpl implements TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final FazendaRepository fazendaRepository;
    private final NegocioRepository negocioRepository;
    private final SafraRepository safraRepository;
    private final FazendaColaboradorRepository fazendaColaboradorRepository;
    private final TransacaoMapper transacaoMapper;

    @Override
    @Transactional
    public TransacaoResponse salvar(TransacaoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaRepository.findById(request.fazendaId())
                .orElseThrow(() -> new BusinessException("Fazenda não encontrada"));

        validarAcesso(fazenda, numeroUsuarioLogado);
        validarVinculo(request.negocioId(), request.safraId());

        Transacao transacao = transacaoMapper.toEntity(request);
        transacao.setAtivo(true);
        transacao.setFazenda(fazenda);
        vincularNegocioOuSafra(transacao, request.negocioId(), request.safraId(), fazenda);

        transacao = transacaoRepository.save(transacao);
        return transacaoMapper.toResponse(transacao);
    }

    @Override
    public Page<TransacaoResponse> buscarPorFazenda(Long fazendaId, LocalDate dataInicio, LocalDate dataFim,
                                                    TipoTransacao tipo, Pageable pageable, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaRepository.findById(fazendaId)
                .orElseThrow(() -> new BusinessException("Fazenda não encontrada"));
        validarAcesso(fazenda, numeroUsuarioLogado);

        return transacaoRepository.buscarComFiltros(fazendaId, dataInicio, dataFim, tipo, pageable)
                .map(transacaoMapper::toResponse);
    }

    @Override
    @Transactional
    public TransacaoResponse atualizar(Long id, TransacaoUpdateRequest request, String numeroUsuarioLogado) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Transação não encontrada"));

        validarAcesso(transacao.getFazenda(), numeroUsuarioLogado);
        validarVinculo(request.negocioId(), request.safraId());

        transacaoMapper.atualizar(request, transacao);
        vincularNegocioOuSafra(transacao, request.negocioId(), request.safraId(), transacao.getFazenda());

        transacao = transacaoRepository.save(transacao);
        return transacaoMapper.toResponse(transacao);
    }

    @Override
    public ResumoFinanceiroResponse buscarResumo(Long fazendaId, LocalDate dataInicio, LocalDate dataFim, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaRepository.findById(fazendaId)
                .orElseThrow(() -> new BusinessException("Fazenda não encontrada"));
        validarAcesso(fazenda, numeroUsuarioLogado);
        return transacaoRepository.buscarResumo(fazendaId, dataInicio, dataFim);
    }

    @Override
    @Transactional
    public void desativar(Long id, String numeroUsuarioLogado) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Transação não encontrada"));
        validarAcesso(transacao.getFazenda(), numeroUsuarioLogado);
        transacao.setAtivo(false);
        transacaoRepository.save(transacao);
    }

    @Override
    @Transactional
    public void reativar(Long id, String numeroUsuarioLogado) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Transação não encontrada"));
        validarAcesso(transacao.getFazenda(), numeroUsuarioLogado);
        transacao.setAtivo(true);
        transacaoRepository.save(transacao);
    }

    private void validarAcesso(Fazenda fazenda, String numeroUsuarioLogado) {
        boolean ehDono = fazenda.getDono().getNumero().equals(numeroUsuarioLogado);
        boolean ehColaborador = fazendaColaboradorRepository
                .existsByFazendaIdAndColaboradorNumero(fazenda.getId(), numeroUsuarioLogado);
        if (!ehDono && !ehColaborador) {
            throw new BusinessException("Você não tem permissão para gerenciar transações dessa fazenda");
        }
    }

    private void validarVinculo(Long negocioId, Long safraId) {
        boolean temNegocio = negocioId != null;
        boolean temSafra = safraId != null;
        if (temNegocio == temSafra) {
            throw new BusinessException(
                    "A transação deve estar vinculada a exatamente um Negócio ou uma Safra, nunca os dois ou nenhum");
        }
    }

    private void vincularNegocioOuSafra(Transacao transacao, Long negocioId, Long safraId, Fazenda fazenda) {
        if (negocioId != null) {
            Negocio negocio = negocioRepository.findById(negocioId)
                    .orElseThrow(() -> new BusinessException("Negócio não encontrado"));
            if (!negocio.getFazenda().getId().equals(fazenda.getId())) {
                throw new BusinessException("O negócio informado não pertence a essa fazenda");
            }
            transacao.setNegocio(negocio);
            transacao.setSafra(null);
        } else {
            Safra safra = safraRepository.findById(safraId)
                    .orElseThrow(() -> new BusinessException("Safra não encontrada"));
            if (!safra.getFazenda().getId().equals(fazenda.getId())) {
                throw new BusinessException("A safra informada não pertence a essa fazenda");
            }
            transacao.setNegocio(null);
            transacao.setSafra(safra);
        }
    }
}