package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.veiculos.PostoPagamentoRequest;
import com.example.fazendalosardo.dto.veiculos.PostoRequest;
import com.example.fazendalosardo.dto.veiculos.PostoResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.model.Posto;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.veiculos.PostoRepository;
import com.example.fazendalosardo.service.PostoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostoServiceImpl implements PostoService {

    private final PostoRepository postoRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;

    @Override
    public PostoResponse salvar(PostoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Posto posto = new Posto();
        posto.setNome(request.nome());
        posto.setFazenda(fazenda);
        posto.setObservacao(request.observacao());
        posto.setSaldoDevedor(BigDecimal.ZERO);
        posto.setAtivo(true);

        return PostoResponse.de(postoRepository.save(posto));
    }

    @Override
    public List<PostoResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return postoRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId).stream()
                .map(PostoResponse::de)
                .toList();
    }

    @Override
    public PostoResponse registrarPagamento(Long postoId, PostoPagamentoRequest request, String numeroUsuarioLogado) {
        Posto posto = postoRepository.findById(postoId)
                .orElseThrow(() -> new NotFoundException("Posto não encontrado"));
        fazendaAcessoHelper.validarAcesso(posto.getFazenda(), numeroUsuarioLogado);
        helper.validarVinculoXor(request.negocioId(), request.safraId());

        if (request.valor().compareTo(posto.getSaldoDevedor()) > 0) {
            throw new BusinessException("O valor do pagamento não pode ser maior que o saldo devedor do posto");
        }

        Negocio negocio = helper.buscarNegocio(request.negocioId());
        Safra safra = helper.buscarSafra(request.safraId());

        String descricao = "Pagamento posto - %s".formatted(posto.getNome());
        helper.gerarTransacaoGasto(posto.getFazenda(), negocio, safra, request.valor(), request.data(), descricao);

        posto.setSaldoDevedor(posto.getSaldoDevedor().subtract(request.valor()));
        return PostoResponse.de(postoRepository.save(posto));
    }
}