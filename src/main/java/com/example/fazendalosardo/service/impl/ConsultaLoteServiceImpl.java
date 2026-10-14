package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.ConsultaLoteRequest;
import com.example.fazendalosardo.dto.animais.ConsultaLoteResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.ConsultaLote;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Lote;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.model.enums.TipoConsulta;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.NegocioRepository;
import com.example.fazendalosardo.repository.animais.ConsultaLoteRepository;
import com.example.fazendalosardo.repository.animais.LoteRepository;
import com.example.fazendalosardo.repository.safras.SafraRepository;
import com.example.fazendalosardo.service.ConsultaLoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaLoteServiceImpl implements ConsultaLoteService {

    private final ConsultaLoteRepository consultaLoteRepository;
    private final LoteRepository loteRepository;
    private final NegocioRepository negocioRepository;
    private final SafraRepository safraRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper transacaoHelper;

    @Override
    @Transactional
    public ConsultaLoteResponse salvar(ConsultaLoteRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Lote lote = loteRepository.findById(request.loteId())
                .orElseThrow(() -> new NotFoundException("Lote não encontrado"));

        if (request.quantidadeAnimais() > lote.getQuantidade()) {
            throw new BusinessException("A quantidade de animais não pode ser maior que a quantidade do lote");
        }

        if (request.negocioId() == null && request.safraId() == null) {
            throw new BusinessException("Informe um negócio ou uma safra");
        }

        Negocio negocio = null;
        Safra safra = null;

        if (request.negocioId() != null) {
            negocio = negocioRepository.findById(request.negocioId())
                    .orElseThrow(() -> new NotFoundException("Negócio não encontrado"));
        } else {
            safra = safraRepository.findById(request.safraId())
                    .orElseThrow(() -> new NotFoundException("Safra não encontrada"));
        }

        ConsultaLote consultaLote = new ConsultaLote();
        consultaLote.setLote(lote);
        consultaLote.setTipo(request.tipo());
        consultaLote.setData(request.data());
        consultaLote.setQuantidadeAnimais(request.quantidadeAnimais());
        consultaLote.setCusto(request.custo());
        consultaLote.setDescricao(request.descricao());
        consultaLote.setObservacao(request.observacao());
        consultaLote.setFazenda(fazenda);
        consultaLote.setNegocio(negocio);
        consultaLote.setSafra(safra);

        consultaLoteRepository.save(consultaLote);

        String descricaoGasto = tipoLabel(request.tipo()) + " - " + lote.getNome();

        transacaoHelper.gerarTransacaoGasto(fazenda, negocio, safra, request.custo(), request.data(), descricaoGasto);

        return toResponse(consultaLote);
    }

    @Override
    public Page<ConsultaLoteResponse> buscarPorLote(Long loteId, int page, int size, String numeroUsuarioLogado) {
        Lote lote = loteRepository.findById(loteId)
                .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
        fazendaAcessoHelper.validarAcesso(lote.getFazenda(), numeroUsuarioLogado);

        Pageable pageable = PageRequest.of(page, size);
        return consultaLoteRepository.buscarPorLote(loteId, pageable).map(this::toResponse);
    }

    private String tipoLabel(TipoConsulta tipo) {
        return switch (tipo) {
            case VACINACAO -> "Vacinação";
            case CONSULTA_VETERINARIA -> "Consulta Veterinária";
            case EXAME -> "Exame";
            case TRATAMENTO -> "Tratamento";
            case OUTRA -> "Outra";
        };
    }

    private ConsultaLoteResponse toResponse(ConsultaLote c) {
        return new ConsultaLoteResponse(
                c.getId(),
                c.getLote().getId(),
                c.getLote().getNome(),
                c.getTipo(),
                c.getData(),
                c.getQuantidadeAnimais(),
                c.getCusto(),
                c.getDescricao(),
                c.getObservacao(),
                c.getNegocio() != null ? c.getNegocio().getNome() : null,
                c.getSafra() != null ? c.getSafra().getNome() : null
        );
    }
}