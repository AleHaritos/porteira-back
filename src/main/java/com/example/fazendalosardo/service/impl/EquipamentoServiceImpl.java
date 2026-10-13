package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.equipamento.EquipamentoRequest;
import com.example.fazendalosardo.dto.equipamento.EquipamentoResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Equipamento;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.repository.equipamento.EquipamentoRepository;
import com.example.fazendalosardo.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EquipamentoServiceImpl implements EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    public EquipamentoResponse salvar(EquipamentoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);
        Equipamento equipamento = new Equipamento();
        equipamento.setNome(request.nome());
        equipamento.setDataCompra(request.dataCompra());
        equipamento.setValorCompra(request.valorCompra());
        equipamento.setObservacao(request.observacao());
        equipamento.setFazenda(fazenda);
        equipamento.setAtivo(true);
        return EquipamentoResponse.de(equipamentoRepository.save(equipamento));
    }

    @Override
    public EquipamentoResponse atualizar(Long id, EquipamentoRequest request, String numeroUsuarioLogado) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));
        fazendaAcessoHelper.validarAcesso(equipamento.getFazenda(), numeroUsuarioLogado);
        equipamento.setNome(request.nome());
        equipamento.setDataCompra(request.dataCompra());
        equipamento.setValorCompra(request.valorCompra());
        equipamento.setObservacao(request.observacao());
        return EquipamentoResponse.de(equipamentoRepository.save(equipamento));
    }

    @Override
    public Page<EquipamentoResponse> buscarPorFazenda(Long fazendaId, Pageable pageable, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return equipamentoRepository.findByFazendaIdAndAtivoTrueOrderByNome(fazendaId, pageable).map(EquipamentoResponse::de);
    }

    @Override
    public EquipamentoResponse buscarPorId(Long id, String numeroUsuarioLogado) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));
        fazendaAcessoHelper.validarAcesso(equipamento.getFazenda(), numeroUsuarioLogado);
        return EquipamentoResponse.de(equipamento);
    }

    @Override
    public void inativar(Long id, String numeroUsuarioLogado) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));
        fazendaAcessoHelper.validarAcesso(equipamento.getFazenda(), numeroUsuarioLogado);
        equipamento.setAtivo(false);
        equipamentoRepository.save(equipamento);
    }
}