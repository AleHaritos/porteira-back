package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.model.Projeto;
import com.example.fazendalosardo.model.Transacao;
import com.example.fazendalosardo.model.enums.TipoTransacao;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.NegocioRepository;
import com.example.fazendalosardo.repository.ProjetoRepository;
import com.example.fazendalosardo.repository.financeiro.TransacaoRepository;
import com.example.fazendalosardo.repository.safras.SafraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class VeiculoTransacaoHelper {

    private final NegocioRepository negocioRepository;
    private final SafraRepository safraRepository;
    private final ProjetoRepository projetoRepository;
    private final TransacaoRepository transacaoRepository;

    public void validarVinculoXor(Long negocioId, Long safraId) {
        boolean temNegocio = negocioId != null;
        boolean temSafra = safraId != null;
        if (temNegocio == temSafra) {
            throw new BusinessException("Informe um negócio OU uma safra, nunca os dois ou nenhum");
        }
    }

    public void validarVinculoXor(Long negocioId, Long safraId, Long projetoId) {
        int quantidade = 0;
        if (negocioId != null) quantidade++;
        if (safraId != null) quantidade++;
        if (projetoId != null) quantidade++;

        if (quantidade != 1) {
            throw new BusinessException("Informe um Negócio, uma Safra ou um Projeto, nunca mais de um ou nenhum");
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

    public Projeto buscarProjeto(Long projetoId) {
        if (projetoId == null) return null;
        return projetoRepository.findById(projetoId)
                .orElseThrow(() -> new NotFoundException("Projeto não encontrado"));
    }

    public Transacao gerarTransacaoGasto(Fazenda fazenda, Negocio negocio, Safra safra,
                                         BigDecimal valor, LocalDate data, String descricao) {
        return gerarTransacaoGasto(fazenda, negocio, safra, null, valor, data, descricao);
    }

    public Transacao gerarTransacaoGasto(Fazenda fazenda, Negocio negocio, Safra safra, Projeto projeto,
                                         BigDecimal valor, LocalDate data, String descricao) {
        Transacao transacao = new Transacao();
        transacao.setTipo(TipoTransacao.GASTO);
        transacao.setValor(valor);
        transacao.setData(data);
        transacao.setDescricao(descricao);
        transacao.setFazenda(fazenda);
        transacao.setNegocio(negocio);
        transacao.setSafra(safra);
        transacao.setProjeto(projeto);
        transacao.setAtivo(true);
        return transacaoRepository.save(transacao);
    }

    public Transacao gerarTransacaoReceita(Fazenda fazenda, Negocio negocio, Safra safra,
                                           BigDecimal valor, LocalDate data, String descricao) {
        Transacao transacao = new Transacao();
        transacao.setTipo(TipoTransacao.RECEITA);
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