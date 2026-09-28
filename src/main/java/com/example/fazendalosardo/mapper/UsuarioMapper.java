package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.UsuarioRequest;
import com.example.fazendalosardo.dto.UsuarioResponse;
import com.example.fazendalosardo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "admin", ignore = true)
    Usuario toEntity(UsuarioRequest request);

    UsuarioResponse toResponse(Usuario usuario);
}
