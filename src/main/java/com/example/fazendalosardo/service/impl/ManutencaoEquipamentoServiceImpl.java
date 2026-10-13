package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoRequest;
import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.FormaPagamentoManutencao;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.equipamento.EquipamentoRepository;
import com.example.fazendalosardo.repository.equipamento.ManutencaoEquipamentoRepository;
import com.example.fazendalosardo.service.FornecedorService;
import com.example.fazendalosardo.service.ManutencaoEquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManutencaoEquipamentoServiceImpl implements ManutencaoEquipamentoService {

    private final ManutencaoEquipamentoRepository manutencaoEquipamentoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final VeiculoTransacaoHelper helper;
    private final FornecedorService fornecedorService;

    @Override
    public ManutencaoEquipamentoResponse salvar(ManutencaoEquipamentoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);
        helper.validarVinculoXor(request.negocioId(), request.safraId());

        Equipamento equipamento = equipamentoRepository.findById(request.equipamentoId())
                .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));

        Negocio negocio = helper.buscarNegocio(request.negocioId());
        Safra safra = helper.buscarSafra(request.safraId());

        String base = (request.descricao() != null && !request.descricao().isBlank())
                ? request.descricao() : "Manutenção";
        String descricao = "%s - %s".formatted(base, equipamento.getNome());

        ManutencaoEquipamento manutencao = new ManutencaoEquipamento();
        manutencao.setEquipamento(equipamento);
        manutencao.setTipo(request.tipo());
        manutencao.setDescricao(request.descricao());
        manutencao.setData(request.data());
        manutencao.setValor(request.valor());
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

        return ManutencaoEquipamentoResponse.de(manutencaoEquipamentoRepository.save(manutencao));
    }

    @Override
    public Page<ManutencaoEquipamentoResponse> buscarPorEquipamento(Long equipamentoId, Pageable pageable, String numeroUsuarioLogado) {
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));
        fazendaAcessoHelper.validarAcesso(equipamento.getFazenda(), numeroUsuarioLogado);
        return manutencaoEquipamentoRepository.buscarPorEquipamento(equipamentoId, pageable).map(ManutencaoEquipamentoResponse::de);
    }
}