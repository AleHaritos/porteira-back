package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.estoque.*;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.estoque.CompraEstoque;
import com.example.fazendalosardo.model.estoque.ProdutoEstoque;
import com.example.fazendalosardo.model.estoque.UsoEstoque;
import com.example.fazendalosardo.repository.estoque.CompraEstoqueRepository;
import com.example.fazendalosardo.repository.estoque.ProdutoEstoqueRepository;
import com.example.fazendalosardo.repository.estoque.UsoEstoqueRepository;
import com.example.fazendalosardo.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueServiceImpl implements EstoqueService {

    private final ProdutoEstoqueRepository produtoEstoqueRepository;
    private final CompraEstoqueRepository compraEstoqueRepository;
    private final UsoEstoqueRepository usoEstoqueRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    @Transactional
    public ProdutoEstoqueResponse criarProduto(ProdutoEstoqueRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        ProdutoEstoque produto = new ProdutoEstoque();
        produto.setFazenda(fazenda);
        produto.setNome(request.nome());
        produto.setValor(BigDecimal.ZERO);
        produto.setQuantidade(BigDecimal.ZERO);

        return ProdutoEstoqueResponse.de(produtoEstoqueRepository.save(produto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEstoqueResponse> listarProdutos(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return produtoEstoqueRepository.findByFazendaIdOrderByNomeAsc(fazendaId).stream()
                .map(ProdutoEstoqueResponse::de)
                .toList();
    }

    @Override
    @Transactional
    public void excluirProduto(Long produtoId, String numeroUsuarioLogado) {
        ProdutoEstoque produto = buscarProdutoComAcesso(produtoId, numeroUsuarioLogado);
        produtoEstoqueRepository.delete(produto);
    }

    @Override
    @Transactional
    public ProdutoEstoqueResponse registrarCompra(Long produtoId, CompraEstoqueRequest request, String numeroUsuarioLogado) {
        ProdutoEstoque produto = buscarProdutoComAcesso(produtoId, numeroUsuarioLogado);

        CompraEstoque compra = new CompraEstoque();
        compra.setProduto(produto);
        compra.setQuantidade(request.quantidade());
        compra.setValorUnitario(request.valorUnitario());
        compra.setData(request.data());
        compra.setObservacao(request.observacao());
        compraEstoqueRepository.save(compra);

        produto.setQuantidade(produto.getQuantidade().add(request.quantidade()));
        produto.setValor(request.valorUnitario());

        return ProdutoEstoqueResponse.de(produtoEstoqueRepository.save(produto));
    }

    @Override
    @Transactional
    public ProdutoEstoqueResponse registrarUso(Long produtoId, UsoEstoqueRequest request, String numeroUsuarioLogado) {
        ProdutoEstoque produto = buscarProdutoComAcesso(produtoId, numeroUsuarioLogado);

        if (request.quantidade().compareTo(produto.getQuantidade()) > 0) {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque");
        }

        UsoEstoque uso = new UsoEstoque();
        uso.setProduto(produto);
        uso.setQuantidade(request.quantidade());
        uso.setData(request.data());
        uso.setObservacao(request.observacao());
        usoEstoqueRepository.save(uso);

        produto.setQuantidade(produto.getQuantidade().subtract(request.quantidade()));

        return ProdutoEstoqueResponse.de(produtoEstoqueRepository.save(produto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraEstoqueResponse> listarCompras(Long produtoId, String numeroUsuarioLogado) {
        buscarProdutoComAcesso(produtoId, numeroUsuarioLogado);
        return compraEstoqueRepository.findByProdutoIdOrderByDataDescIdDesc(produtoId).stream()
                .map(CompraEstoqueResponse::de)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsoEstoqueResponse> listarUsos(Long produtoId, String numeroUsuarioLogado) {
        buscarProdutoComAcesso(produtoId, numeroUsuarioLogado);
        return usoEstoqueRepository.findByProdutoIdOrderByDataDescIdDesc(produtoId).stream()
                .map(UsoEstoqueResponse::de)
                .toList();
    }

    private ProdutoEstoque buscarProdutoComAcesso(Long produtoId, String numeroUsuarioLogado) {
        ProdutoEstoque produto = produtoEstoqueRepository.findById(produtoId)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        fazendaAcessoHelper.validarAcesso(produto.getFazenda(), numeroUsuarioLogado);
        return produto;
    }
}