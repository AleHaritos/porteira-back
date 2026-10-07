package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.safraDTO.ProdutoSafraRequest;
import com.example.fazendalosardo.dto.safraDTO.ProdutoSafraResponse;
import com.example.fazendalosardo.dto.safraDTO.ProdutoSafraUpdateRequest;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.mapper.ProdutoSafraMapper;
import com.example.fazendalosardo.model.safras.ProdutoSafra;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.safras.ManejoRepository;
import com.example.fazendalosardo.repository.safras.ProdutoSafraRepository;
import com.example.fazendalosardo.repository.safras.SafraRepository;
import com.example.fazendalosardo.service.ProdutoSafraService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProdutoSafraServiceImpl implements ProdutoSafraService {

    private final ProdutoSafraRepository produtoSafraRepository;
    private final SafraRepository safraRepository;
    private final ManejoRepository manejoRepository;
    private final ProdutoSafraMapper produtoSafraMapper;

    @Override
    @Transactional
    public ProdutoSafraResponse salvar(ProdutoSafraRequest request) {
        Safra safra = safraRepository.findById(request.safraId())
                .orElseThrow(() -> new NotFoundException("Safra não encontrada"));

        ProdutoSafra produtoSafra = produtoSafraMapper.toEntity(request);
        produtoSafra.setSafra(safra);

        return produtoSafraMapper.toResponse(produtoSafraRepository.save(produtoSafra));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProdutoSafraResponse> buscarPorSafra(Long safraId, Pageable pageable) {
        return produtoSafraRepository.findBySafraId(safraId, pageable)
                .map(produtoSafraMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoSafraResponse> listarPorSafra(Long safraId) {
        return produtoSafraRepository.findBySafraId(safraId)
                .stream()
                .map(produtoSafraMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProdutoSafraResponse atualizar(Long id, ProdutoSafraUpdateRequest request) {
        ProdutoSafra produtoSafra = produtoSafraRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        produtoSafraMapper.atualizar(request, produtoSafra);

        return produtoSafraMapper.toResponse(produtoSafraRepository.save(produtoSafra));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!produtoSafraRepository.existsById(id)) {
            throw new NotFoundException("Produto não encontrado");
        }

        if (manejoRepository.existsByProdutoSafraId(id)) {
            throw new BusinessException("Não é possível excluir um produto que já foi utilizado em manejos");
        }

        produtoSafraRepository.deleteById(id);
    }
}
