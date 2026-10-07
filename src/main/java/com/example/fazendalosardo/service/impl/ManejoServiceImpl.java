package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.safraDTO.ManejoRequest;
import com.example.fazendalosardo.dto.safraDTO.ManejoResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.mapper.ManejoMapper;
import com.example.fazendalosardo.model.safras.Manejo;
import com.example.fazendalosardo.model.safras.ProdutoSafra;
import com.example.fazendalosardo.model.safras.Talhao;
import com.example.fazendalosardo.repository.safras.ManejoRepository;
import com.example.fazendalosardo.repository.safras.ProdutoSafraRepository;
import com.example.fazendalosardo.repository.safras.TalhaoRepository;
import com.example.fazendalosardo.service.ManejoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManejoServiceImpl implements ManejoService {

    private final ManejoRepository manejoRepository;
    private final TalhaoRepository talhaoRepository;
    private final ProdutoSafraRepository produtoSafraRepository;
    private final ManejoMapper manejoMapper;

    @Override
    @Transactional
    public ManejoResponse salvar(ManejoRequest request) {
        validarProduto(request);

        Talhao talhao = talhaoRepository.findById(request.talhaoId())
                .orElseThrow(() -> new NotFoundException("Talhão não encontrado"));

        Manejo manejo = manejoMapper.toEntity(request);
        manejo.setTalhao(talhao);

        if (request.produtoSafraId() != null) {
            ProdutoSafra produtoSafra = produtoSafraRepository.findById(request.produtoSafraId())
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
            manejo.setProdutoSafra(produtoSafra);
        }

        return manejoMapper.toResponse(manejoRepository.save(manejo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManejoResponse> buscarPorTalhao(Long talhaoId, Pageable pageable) {
        return manejoRepository.findByTalhaoId(talhaoId, pageable)
                .map(manejoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManejoResponse> listarPorTalhao(Long talhaoId) {
        return manejoRepository.findByTalhaoId(talhaoId)
                .stream()
                .map(manejoMapper::toResponse)
                .toList();
    }

    private void validarProduto(ManejoRequest request) {
        boolean temProduto = request.produtoSafraId() != null;
        boolean temQuantidade = request.quantidadeProduto() != null;

        if (temProduto != temQuantidade) {
            throw new BusinessException("Produto e quantidade devem ser informados juntos");
        }
    }
}