package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.safraDTO.CustoFixoTalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.CustoFixoTalhaoResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.safras.CustoFixoTalhao;
import com.example.fazendalosardo.model.safras.Talhao;
import com.example.fazendalosardo.repository.safras.CustoFixoTalhaoRepository;
import com.example.fazendalosardo.repository.safras.TalhaoRepository;
import com.example.fazendalosardo.service.CustoFixoTalhaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustoFixoTalhaoServiceImpl implements CustoFixoTalhaoService {

    private final CustoFixoTalhaoRepository custoFixoTalhaoRepository;
    private final TalhaoRepository talhaoRepository;

    @Override
    @Transactional
    public CustoFixoTalhaoResponse salvar(CustoFixoTalhaoRequest request) {
        Talhao talhao = talhaoRepository.findById(request.talhaoId())
                .orElseThrow(() -> new NotFoundException("Talhão não encontrado"));

        CustoFixoTalhao custo = new CustoFixoTalhao();
        custo.setTalhao(talhao);
        custo.setDescricao(request.descricao());
        custo.setValor(request.valor());

        return CustoFixoTalhaoResponse.de(custoFixoTalhaoRepository.save(custo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustoFixoTalhaoResponse> listarPorTalhao(Long talhaoId) {
        return custoFixoTalhaoRepository.findByTalhaoIdOrderByIdAsc(talhaoId)
                .stream()
                .map(CustoFixoTalhaoResponse::de)
                .toList();
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!custoFixoTalhaoRepository.existsById(id)) {
            throw new NotFoundException("Custo fixo não encontrado");
        }
        custoFixoTalhaoRepository.deleteById(id);
    }
}