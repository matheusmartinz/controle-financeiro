package com.mfinancas.api.dto;

import com.mfinancas.api.model.Usuario;

import java.util.UUID;

public record UsuarioTO(UUID uuid,
                        String email,
                        String senha) {


    public UsuarioTO(Usuario usuario) {
        this(usuario.getUuid(), usuario.getEmail(), "SEGREDO");
    }
}
