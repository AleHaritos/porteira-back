package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.financeiro.ContaPendenteRequest;
import com.example.fazendalosardo.dto.financeiro.ContaPendenteResponse;
import com.example.fazendalosardo.dto.financeiro.ParcelaContaPendenteResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.model.enums.TipoTransacao;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.FazendaColaboradorRepository;
import com.example.fazendalosardo.repository.FazendaRepository;
import com.example.fazendalosardo.repository.NegocioRepository;
import com.example.fazendalosardo.repository.financeiro.ContaPendenteRepository;
import com.example.fazendalosardo.repository.financeiro.ParcelaContaPendenteRepository;
import com.example.fazendalosardo.repository.financeiro.TransacaoRepository;
import com.example.fazendalosardo.repository.safras.SafraRepository;
import com.example.fazendalosardo.service.ContaPendenteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaPendenteServiceImpl implements ContaPendenteService {

    private final ContaPendenteRepository contaPendenteRepository;
    private final ParcelaContaPendenteRepository parcelaContaPendenteRepository;
    private final FazendaColaboradorRepository fazendaColaboradorRepository;
    private final TransacaoRepository transacaoRepository;
    private final FazendaRepository fazendaRepository;
    private final NegocioRepository negocioRepository;
    private final SafraRepository safraRepository;


    @Override
    public ContaPendenteResponse salvar(ContaPendenteRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaRepository.findById(request.fazendaId())
                .orElseThrow(() -> new EntityNotFoundException("Fazenda não encontrada"));

        validarAcesso(fazenda, numeroUsuarioLogado);

        boolean temNegocio = request.negocioId() != null;
        boolean temSafra = request.safraId() != null;
        if (temNegocio == temSafra) {
            throw new IllegalArgumentException("Informe exatamente um vínculo: negócio ou safra");
        }

        if (request.valoresParcelas() == null || request.valoresParcelas().isEmpty()) {
            throw new IllegalArgumentException("Informe ao menos uma parcela");
        }

        BigDecimal valorTotal = request.valoresParcelas().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        ContaPendente conta = new ContaPendente();
        conta.setDescricao(request.descricao());
        conta.setValorTotal(valorTotal);
        conta.setNumeroParcelas(request.valoresParcelas().size());
        conta.setTipo(request.tipo());
        conta.setFazenda(fazenda);
        conta.setObservacao(request.observacao());
        conta.setAtivo(true);

        if (temNegocio) {
            Negocio negocio = negocioRepository.findById(request.negocioId())
                    .orElseThrow(() -> new EntityNotFoundException("Negócio não encontrado"));
            conta.setNegocio(negocio);
        } else {
            Safra safra = safraRepository.findById(request.safraId())
                    .orElseThrow(() -> new EntityNotFoundException("Safra não encontrada"));
            conta.setSafra(safra);
        }

        conta.setParcelas(gerarParcelas(conta, request.valoresParcelas(), request.primeiraDataVencimento()));

        return toContaResponse(contaPendenteRepository.save(conta));
    }

    private List<ParcelaContaPendente> gerarParcelas(ContaPendente conta, List<BigDecimal> valoresParcelas,
                                                     LocalDate primeiraDataVencimento) {
        List<ParcelaContaPendente> parcelas = new ArrayList<>();

        for (int i = 0; i < valoresParcelas.size(); i++) {
            ParcelaContaPendente parcela = new ParcelaContaPendente();
            parcela.setContaPendente(conta);
            parcela.setNumeroParcela(i + 1);
            parcela.setDataVencimento(primeiraDataVencimento.plusMonths(i));
            parcela.setStatus(StatusParcela.PENDENTE);
            parcela.setValor(valoresParcelas.get(i));
            parcelas.add(parcela);
        }

        return parcelas;
    }

    @Override
    public Page<ParcelaContaPendenteResponse> buscarParcelasPorFazenda(Long fazendaId, StatusParcela status, TipoTransacao tipo,
                                                                       Pageable pageable, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaRepository.findById(fazendaId)
                .orElseThrow(() -> new EntityNotFoundException("Fazenda não encontrada"));

        validarAcesso(fazenda, numeroUsuarioLogado);

        return parcelaContaPendenteRepository.buscarPorFazenda(fazendaId, status, tipo, pageable)
                .map(this::toParcelaResponse);
    }

    @Override
    public ParcelaContaPendenteResponse darBaixa(Long parcelaId, LocalDate dataBaixa, String numeroUsuarioLogado) {
        ParcelaContaPendente parcela = parcelaContaPendenteRepository.findById(parcelaId)
                .orElseThrow(() -> new EntityNotFoundException("Parcela não encontrada"));

        ContaPendente conta = parcela.getContaPendente();
        validarAcesso(conta.getFazenda(), numeroUsuarioLogado);

        if (parcela.getStatus() == StatusParcela.BAIXADO) {
            throw new IllegalStateException("Parcela já foi baixada");
        }

        LocalDate dataEfetiva = dataBaixa != null ? dataBaixa : LocalDate.now();

        Transacao transacao = new Transacao();
        transacao.setData(dataEfetiva);
        transacao.setValor(parcela.getValor());
        transacao.setTipo(conta.getTipo());
        transacao.setDescricao(formatarDescricaoTransacao(conta, parcela));
        transacao.setFazenda(conta.getFazenda());
        transacao.setNegocio(conta.getNegocio());
        transacao.setSafra(conta.getSafra());
        transacao.setAtivo(true);

        transacaoRepository.save(transacao);

        parcela.setStatus(StatusParcela.BAIXADO);
        parcela.setDataBaixa(dataEfetiva);
        parcela.setTransacao(transacao);
        parcelaContaPendenteRepository.save(parcela);

        return toParcelaResponse(parcela);
    }

    private String formatarDescricaoTransacao(ContaPendente conta, ParcelaContaPendente parcela) {
        String base = (conta.getDescricao() == null || conta.getDescricao().isBlank())
                ? "Conta pendente" : conta.getDescricao();

        return conta.getNumeroParcelas() > 1
                ? "%s (Parcela %d/%d)".formatted(base, parcela.getNumeroParcela(), conta.getNumeroParcelas())
                : base;
    }

    private ContaPendenteResponse toContaResponse(ContaPendente c) {
        return new ContaPendenteResponse(
                c.getId(), c.getDescricao(), c.getValorTotal(), c.getNumeroParcelas(), c.getTipo(),
                c.getFazenda().getId(),
                c.getNegocio() != null ? c.getNegocio().getId() : null,
                c.getNegocio() != null ? c.getNegocio().getNome() : null,
                c.getSafra() != null ? c.getSafra().getId() : null,
                c.getSafra() != null ? c.getSafra().getNome() : null,
                c.getObservacao());
    }

    private ParcelaContaPendenteResponse toParcelaResponse(ParcelaContaPendente p) {
        ContaPendente conta = p.getContaPendente();
        return new ParcelaContaPendenteResponse(
                p.getId(), p.getNumeroParcela(), conta.getNumeroParcelas(), p.getValor(), p.getDataVencimento(),
                p.getStatus().name(), p.getDataBaixa(), conta.getTipo(), conta.getId(), conta.getDescricao(),
                conta.getNegocio() != null ? conta.getNegocio().getNome() : null,
                conta.getSafra() != null ? conta.getSafra().getNome() : null);
    }

    private void validarAcesso(Fazenda fazenda, String numeroUsuarioLogado) {
        boolean ehDono = fazenda.getDono().getNumero().equals(numeroUsuarioLogado);
        boolean ehColaborador = fazendaColaboradorRepository
                .existsByFazendaIdAndColaboradorNumero(fazenda.getId(), numeroUsuarioLogado);
        if (!ehDono && !ehColaborador) {
            throw new BusinessException("Você não tem permissão para gerenciar transações dessa fazenda");
        }
    }
}