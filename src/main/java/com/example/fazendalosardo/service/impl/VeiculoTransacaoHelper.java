package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.model.Transacao;
import com.example.fazendalosardo.model.enums.TipoTransacao;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.NegocioRepository;
import com.example.fazendalosardo.repository.financeiro.TransacaoRepository;
import com.example.fazendalosardo.repository.safras.SafraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class VeiculoTransacaoHelper {

    private final NegocioRepository negocioRepository;
    private final SafraRepository safraRepository;
    private final TransacaoRepository transacaoRepository;

    public void validarVinculoXor(Long negocioId, Long safraId) {
        boolean temNegocio = negocioId != null;
        boolean temSafra = safraId != null;
        if (temNegocio == temSafra) {
            throw new BusinessException("Informe um negócio OU uma safra, nunca os dois ou nenhum");
        }
    }

    public Negocio buscarNegocio(Long negocioId) {
        if (negocioId == null) return null;
        return negocioRepository.findById(negocioId)
                .orElseThrow(() -> new NotFoundException("Negócio não encontrado"));
    }

    public Safra buscarSafra(Long safraId) {
        if (safraId == null) return null;
        return safraRepository.findById(safraId)
                .orElseThrow(() -> new NotFoundException("Safra não encontrada"));
    }

    public Transacao gerarTransacaoGasto(Fazenda fazenda, Negocio negocio, Safra safra,
                                         BigDecimal valor, LocalDate data, String descricao) {
        Transacao transacao = new Transacao();
        transacao.setTipo(TipoTransacao.GASTO);
        transacao.setValor(valor);
        transacao.setData(data);
        transacao.setDescricao(descricao);
        transacao.setFazenda(fazenda);
        transacao.setNegocio(negocio);
        transacao.setSafra(safra);
        transacao.setAtivo(true);
        return transacaoRepository.save(transacao);
    }
}