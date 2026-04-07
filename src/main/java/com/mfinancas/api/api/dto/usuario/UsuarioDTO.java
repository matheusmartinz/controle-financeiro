package com.mfinancas.api.api.dto.usuario;

import com.mfinancas.api.domain.entity.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UsuarioDTO(UUID uuid,
                         String nome,
                         @NotBlank(message = "Obrigatório inserir email.") String email,
                         @NotBlank(message = "Obrigatório informar senha.") String senha
) {


    public UsuarioDTO(Usuario usuario) {
        this(usuario.getUuid(),
                usuario.getNome(),
                usuario.getEmail(),
                ""
        );
    }

}
