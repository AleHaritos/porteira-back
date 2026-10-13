package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.veiculos.ManutencaoRequest;
import com.example.fazendalosardo.dto.veiculos.ManutencaoResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.FormaPagamentoManutencao;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.veiculos.ManutencaoRepository;
import com.example.fazendalosardo.repository.veiculos.VeiculoRepository;
import com.example.fazendalosardo.service.FornecedorService;
import com.example.fazendalosardo.service.ManutencaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManutencaoServiceImpl implements ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;
    private final VeiculoRepository veiculoRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;
    private final FornecedorService fornecedorService;

    @Override
    public ManutencaoResponse salvar(ManutencaoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);
        helper.validarVinculoXor(request.negocioId(), request.safraId());

        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado"));

        Negocio negocio = helper.buscarNegocio(request.negocioId());
        Safra safra = helper.buscarSafra(request.safraId());

        String base = (request.descricao() != null && !request.descricao().isBlank())
                ? request.descricao() : "Manutenção";
        String descricao = "%s - %s".formatted(base, veiculo.getNome());

        Manutencao manutencao = new Manutencao();
        manutencao.setVeiculo(veiculo);
        manutencao.setTipo(request.tipo());
        manutencao.setDescricao(request.descricao());
        manutencao.setData(request.data());
        manutencao.setValor(request.valor());
        manutencao.setKmAtual(request.kmAtual());
        manutencao.setObservacao(request.observacao());
        manutencao.setFazenda(fazenda);
        manutencao.setNegocio(negocio);
        manutencao.setSafra(safra);
        manutencao.setFormaPagamento(request.formaPagamento());
        manutencao.setAtivo(true);

        if (request.formaPagamento() == FormaPagamentoManutencao.A_VISTA) {
            Transacao transacao = helper.gerarTransacaoGasto(fazenda, negocio, safra, request.valor(), request.data(), descricao);
            manutencao.setTransacao(transacao);
        } else {
            if (request.fornecedorId() == null) {
                throw new BusinessException("Informe o fornecedor para manutenção no fiado");
            }
            Fornecedor fornecedor = fornecedorService.acumularDivida(request.fornecedorId(), request.valor(), numeroUsuarioLogado);
            manutencao.setFornecedor(fornecedor);
        }

        return ManutencaoResponse.de(manutencaoRepository.save(manutencao));
    }

    @Override
    public Page<ManutencaoResponse> buscarPorVeiculo(Long veiculoId, Pageable pageable, String numeroUsuarioLogado) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
        fazendaAcessoHelper.validarAcesso(veiculo.getFazenda(), numeroUsuarioLogado);
        return manutencaoRepository.buscarPorVeiculo(veiculoId, pageable).map(ManutencaoResponse::de);
    }
}