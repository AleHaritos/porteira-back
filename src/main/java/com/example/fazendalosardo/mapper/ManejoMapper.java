package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.safraDTO.ManejoRequest;
import com.example.fazendalosardo.dto.safraDTO.ManejoResponse;
import com.example.fazendalosardo.model.safras.Manejo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ManejoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "talhao", ignore = true)
    @Mapping(target = "produtoSafra", ignore = true)
    Manejo toEntity(ManejoRequest request);

    @Mapping(target = "talhaoId", source = "talhao.id")
    @Mapping(target = "talhaoNome", source = "talhao.nome")
    @Mapping(target = "produtoSafraId", source = "produtoSafra.id")
    @Mapping(target = "produtoSafraNome", source = "produtoSafra.nome")
    @Mapping(target = "custoTotal", expression = "java(calcularCustoTotal(manejo))")
    ManejoResponse toResponse(Manejo manejo);

    default BigDecimal calcularCustoTotal(Manejo manejo) {
        if (manejo.getProdutoSafra() == null || manejo.getQuantidadeProduto() == null) {
            return BigDecimal.ZERO;
        }
        return manejo.getProdutoSafra().getCusto().multiply(manejo.getQuantidadeProduto());
    }
}