package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.safraDTO.ItemManejoRequest;
import com.example.fazendalosardo.dto.safraDTO.ManejoRequest;
import com.example.fazendalosardo.dto.safraDTO.ManejoResponse;
import com.example.fazendalosardo.dto.safraDTO.ManejoUpdateRequest;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.mapper.ManejoMapper;
import com.example.fazendalosardo.model.safras.ItemManejo;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        validarItens(request.itens());

        Talhao talhao = talhaoRepository.findById(request.talhaoId())
                .orElseThrow(() -> new NotFoundException("Talhão não encontrado"));

        Manejo manejo = manejoMapper.toEntity(request);
        manejo.setTalhao(talhao);
        manejo.setItens(new ArrayList<>());

        manejo = manejoRepository.save(manejo);

        List<ItemManejo> itens = montarItens(request.itens(), manejo);
        manejo.getItens().addAll(itens);
        manejo = manejoRepository.save(manejo);

        return manejoMapper.toResponse(manejo);
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

    @Override
    @Transactional
    public ManejoResponse atualizar(Long id, ManejoUpdateRequest request) {
        Manejo manejo = manejoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Manejo não encontrado"));

        validarItens(request.itens());

        manejoMapper.atualizar(request, manejo);

        manejo.getItens().clear();
        List<ItemManejo> novosItens = montarItens(request.itens(), manejo);
        manejo.getItens().addAll(novosItens);

        manejo = manejoRepository.save(manejo);
        return manejoMapper.toResponse(manejo);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!manejoRepository.existsById(id)) {
            throw new NotFoundException("Manejo não encontrado");
        }
        manejoRepository.deleteById(id);
    }

    private void validarItens(List<ItemManejoRequest> itens) {
        if (itens == null || itens.isEmpty()) return;

        Set<Long> produtosVistos = new HashSet<>();
        for (ItemManejoRequest item : itens) {
            if (item.produtoSafraId() == null || item.quantidade() == null) {
                throw new BusinessException("Produto e quantidade são obrigatórios em cada item");
            }
            if (!produtosVistos.add(item.produtoSafraId())) {
                throw new BusinessException("O mesmo produto não pode aparecer duas vezes no mesmo manejo");
            }
        }
    }

    private List<ItemManejo> montarItens(List<ItemManejoRequest> requests, Manejo manejo) {
        if (requests == null) return new ArrayList<>();

        return requests.stream().map(req -> {
            ProdutoSafra produto = produtoSafraRepository.findById(req.produtoSafraId())
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
            ItemManejo item = new ItemManejo();
            item.setManejo(manejo);
            item.setProdutoSafra(produto);
            item.setQuantidade(req.quantidade());
            return item;
        }).collect(Collectors.toList());
    }
}