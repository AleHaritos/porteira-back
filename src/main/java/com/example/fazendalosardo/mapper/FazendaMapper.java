package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.FazendaRequest;
import com.example.fazendalosardo.dto.FazendaResponse;
import com.example.fazendalosardo.dto.FazendaResumoResponse;
import com.example.fazendalosardo.dto.UsuarioDTO;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FazendaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dono", ignore = true)
    @Mapping(target = "colaboradores", ignore = true)
    Fazenda toEntity(FazendaRequest request);

    @Mapping(target = "dono", source = "fazenda.dono")
    @Mapping(target = "colaboradores", source = "colaboradores")
    FazendaResponse toResponse(Fazenda fazenda, List<UsuarioDTO> colaboradores);

    FazendaResumoResponse toResumoResponse(Fazenda fazenda);

    UsuarioDTO toResumo(Usuario usuario);
}