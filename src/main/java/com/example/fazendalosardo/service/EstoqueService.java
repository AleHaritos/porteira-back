package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.estoque.*;

import java.util.List;

public interface EstoqueService {
    ProdutoEstoqueResponse criarProduto(ProdutoEstoqueRequest request, String numeroUsuarioLogado);
    List<ProdutoEstoqueResponse> listarProdutos(Long fazendaId, String numeroUsuarioLogado);
    void excluirProduto(Long produtoId, String numeroUsuarioLogado);

    ProdutoEstoqueResponse registrarCompra(Long produtoId, CompraEstoqueRequest request, String numeroUsuarioLogado);
    ProdutoEstoqueResponse registrarUso(Long produtoId, UsoEstoqueRequest request, String numeroUsuarioLogado);

    List<CompraEstoqueResponse> listarCompras(Long produtoId, String numeroUsuarioLogado);
    List<UsoEstoqueResponse> listarUsos(Long produtoId, String numeroUsuarioLogado);
}