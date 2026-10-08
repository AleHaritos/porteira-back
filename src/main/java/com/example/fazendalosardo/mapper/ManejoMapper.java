package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.safraDTO.ItemManejoResponse;
import com.example.fazendalosardo.dto.safraDTO.ManejoRequest;
import com.example.fazendalosardo.dto.safraDTO.ManejoResponse;
import com.example.fazendalosardo.dto.safraDTO.ManejoUpdateRequest;
import com.example.fazendalosardo.model.safras.ItemManejo;
import com.example.fazendalosardo.model.safras.Manejo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ManejoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "talhao", ignore = true)
    @Mapping(target = "itens", ignore = true)
    Manejo toEntity(ManejoRequest request);

    @Mapping(target = "talhaoId", source = "talhao.id")
    @Mapping(target = "talhaoNome", source = "talhao.nome")
    @Mapping(target = "itens", expression = "java(mapearItens(manejo))")
    @Mapping(target = "custoTotal", expression = "java(calcularCustoTotal(manejo))")
    ManejoResponse toResponse(Manejo manejo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "talhao", ignore = true)
    @Mapping(target = "itens", ignore = true)
    void atualizar(ManejoUpdateRequest request, @MappingTarget Manejo manejo);

    default List<ItemManejoResponse> mapearItens(Manejo manejo) {
        if (manejo.getItens() == null) return List.of();
        return manejo.getItens().stream()
                .map(item -> new ItemManejoResponse(
                        item.getId(),
                        item.getProdutoSafra().getId(),
                        item.getProdutoSafra().getNome(),
                        item.getQuantidade(),
                        item.getProdutoSafra().getCusto(),
                        item.getQuantidade().multiply(item.getProdutoSafra().getCusto())
                ))
                .toList();
    }

    default BigDecimal calcularCustoTotal(Manejo manejo) {
        if (manejo.getItens() == null || manejo.getItens().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return manejo.getItens().stream()
                .map(item -> item.getQuantidade().multiply(item.getProdutoSafra().getCusto()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}