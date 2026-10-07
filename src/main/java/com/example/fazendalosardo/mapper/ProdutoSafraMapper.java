package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.safraDTO.ProdutoSafraRequest;
import com.example.fazendalosardo.dto.safraDTO.ProdutoSafraResponse;
import com.example.fazendalosardo.dto.safraDTO.ProdutoSafraUpdateRequest;
import com.example.fazendalosardo.model.safras.ProdutoSafra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProdutoSafraMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "safra", ignore = true)
    ProdutoSafra toEntity(ProdutoSafraRequest request);

    @Mapping(target = "safraId", source = "safra.id")
    @Mapping(target = "safraNome", source = "safra.nome")
    ProdutoSafraResponse toResponse(ProdutoSafra produtoSafra);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "safra", ignore = true)
    void atualizar(ProdutoSafraUpdateRequest request, @MappingTarget ProdutoSafra produtoSafra);
}
