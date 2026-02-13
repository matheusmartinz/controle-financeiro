package com.mfinancas.api.dto;

import com.mfinancas.api.model.Usuario;

import java.util.UUID;

public record UsuarioTO(UUID uuidUsuario, String nome,
                        String email,
                        String senha) {


    public UsuarioTO(Usuario usuario) {
        this(usuario.getUuid(), usuario.getNome(), usuario.getEmail(), usuario.getSenha());
    }
}
