package com.example.fazendalosardo.service.impl;


import com.example.fazendalosardo.dto.veiculos.VeiculoRequest;
import com.example.fazendalosardo.dto.veiculos.VeiculoResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Veiculo;
import com.example.fazendalosardo.repository.veiculos.VeiculoRepository;
import com.example.fazendalosardo.service.VeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    public VeiculoResponse salvar(VeiculoRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        if (veiculoRepository.existsByPlacaIgnoreCase(request.placa())) {
            throw new BusinessException("Já existe um veículo cadastrado com essa placa");
        }

        Veiculo veiculo = new Veiculo();
        preencher(veiculo, request, fazenda);
        return VeiculoResponse.de(veiculoRepository.save(veiculo));
    }

    @Override
    public VeiculoResponse atualizar(Long id, VeiculoRequest request, String numeroUsuarioLogado) {
        Veiculo veiculo = buscarEntidade(id, numeroUsuarioLogado);
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        if (!veiculo.getPlaca().equalsIgnoreCase(request.placa())
                && veiculoRepository.existsByPlacaIgnoreCase(request.placa())) {
            throw new BusinessException("Já existe um veículo cadastrado com essa placa");
        }

        preencher(veiculo, request, fazenda);
        return VeiculoResponse.de(veiculoRepository.save(veiculo));
    }

    @Override
    public Page<VeiculoResponse> buscarPorFazenda(Long fazendaId, Pageable pageable, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return veiculoRepository.buscarPorFazenda(fazendaId, pageable).map(VeiculoResponse::de);
    }

    @Override
    public VeiculoResponse buscarPorId(Long id, String numeroUsuarioLogado) {
        return VeiculoResponse.de(buscarEntidade(id, numeroUsuarioLogado));
    }

    @Override
    public void inativar(Long id, String numeroUsuarioLogado) {
        Veiculo veiculo = buscarEntidade(id, numeroUsuarioLogado);
        veiculo.setAtivo(false);
        veiculoRepository.save(veiculo);
    }

    private Veiculo buscarEntidade(Long id, String numeroUsuarioLogado) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
        fazendaAcessoHelper.validarAcesso(veiculo.getFazenda(), numeroUsuarioLogado);
        return veiculo;
    }

    private void preencher(Veiculo veiculo, VeiculoRequest request, Fazenda fazenda) {
        veiculo.setNome(request.nome());
        veiculo.setPlaca(request.placa());
        veiculo.setDataCompra(request.dataCompra());
        veiculo.setObservacao(request.observacao());
        veiculo.setTipo(request.tipo());
        veiculo.setFazenda(fazenda);
        if (veiculo.getAtivo() == null) veiculo.setAtivo(true);
    }
}
