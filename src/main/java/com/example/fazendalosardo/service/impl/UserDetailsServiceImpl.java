package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.model.Usuario;
import com.example.fazendalosardo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String numero) {
        Usuario usuario = usuarioRepository.findByNumero(numero)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String role = Boolean.TRUE.equals(usuario.getAdmin()) ? "ROLE_ADMIN" : "ROLE_USER";

        return User.withUsername(usuario.getNumero())
                .password(usuario.getSenha())
                .authorities(role)
                .build();
    }
}
