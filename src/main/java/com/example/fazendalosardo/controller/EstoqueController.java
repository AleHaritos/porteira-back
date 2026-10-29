package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.estoque.*;
import com.example.fazendalosardo.service.EstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;

    @PostMapping("/produto")
    public ResponseEntity<ProdutoEstoqueResponse> criarProduto(@Valid @RequestBody ProdutoEstoqueRequest request,
                                                               Authentication authentication) {
        return ResponseEntity.ok(estoqueService.criarProduto(request, authentication.getName()));
    }

    @GetMapping("/produto/fazenda/{fazendaId}")
    public ResponseEntity<List<ProdutoEstoqueResponse>> listarProdutos(@PathVariable Long fazendaId,
                                                                       Authentication authentication) {
        return ResponseEntity.ok(estoqueService.listarProdutos(fazendaId, authentication.getName()));
    }

    @DeleteMapping("/produto/{produtoId}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long produtoId, Authentication authentication) {
        estoqueService.excluirProduto(produtoId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/produto/{produtoId}/compra")
    public ResponseEntity<ProdutoEstoqueResponse> registrarCompra(@PathVariable Long produtoId,
                                                                  @Valid @RequestBody CompraEstoqueRequest request,
                                                                  Authentication authentication) {
        return ResponseEntity.ok(estoqueService.registrarCompra(produtoId, request, authentication.getName()));
    }

    @PostMapping("/produto/{produtoId}/uso")
    public ResponseEntity<ProdutoEstoqueResponse> registrarUso(@PathVariable Long produtoId,
                                                               @Valid @RequestBody UsoEstoqueRequest request,
                                                               Authentication authentication) {
        return ResponseEntity.ok(estoqueService.registrarUso(produtoId, request, authentication.getName()));
    }

    @GetMapping("/produto/{produtoId}/compras")
    public ResponseEntity<List<CompraEstoqueResponse>> listarCompras(@PathVariable Long produtoId,
                                                                     Authentication authentication) {
        return ResponseEntity.ok(estoqueService.listarCompras(produtoId, authentication.getName()));
    }

    @GetMapping("/produto/{produtoId}/usos")
    public ResponseEntity<List<UsoEstoqueResponse>> listarUsos(@PathVariable Long produtoId,
                                                               Authentication authentication) {
        return ResponseEntity.ok(estoqueService.listarUsos(produtoId, authentication.getName()));
    }
}