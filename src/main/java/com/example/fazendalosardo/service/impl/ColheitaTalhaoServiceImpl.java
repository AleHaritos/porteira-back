package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.safraDTO.ColheitaTalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.ColheitaTalhaoResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.safras.ColheitaTalhao;
import com.example.fazendalosardo.model.safras.Talhao;
import com.example.fazendalosardo.repository.safras.ColheitaTalhaoRepository;
import com.example.fazendalosardo.repository.safras.TalhaoRepository;
import com.example.fazendalosardo.service.ColheitaTalhaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColheitaTalhaoServiceImpl implements ColheitaTalhaoService {

    private final ColheitaTalhaoRepository colheitaTalhaoRepository;
    private final TalhaoRepository talhaoRepository;

    @Override
    @Transactional
    public ColheitaTalhaoResponse salvar(ColheitaTalhaoRequest request) {
        Talhao talhao = talhaoRepository.findById(request.talhaoId())
                .orElseThrow(() -> new NotFoundException("Talhão não encontrado"));

        ColheitaTalhao colheita = new ColheitaTalhao();
        colheita.setTalhao(talhao);
        colheita.setDescricao(request.descricao());
        colheita.setQuantidadeColhida(request.quantidadeColhida());
        colheita.setValorUnitario(request.valorUnitario());

        return ColheitaTalhaoResponse.de(colheitaTalhaoRepository.save(colheita));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColheitaTalhaoResponse> listarPorTalhao(Long talhaoId) {
        return colheitaTalhaoRepository.findByTalhaoIdOrderByIdAsc(talhaoId)
                .stream()
                .map(ColheitaTalhaoResponse::de)
                .toList();
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!colheitaTalhaoRepository.existsById(id)) {
            throw new NotFoundException("Colheita não encontrada");
        }
        colheitaTalhaoRepository.deleteById(id);
    }
}