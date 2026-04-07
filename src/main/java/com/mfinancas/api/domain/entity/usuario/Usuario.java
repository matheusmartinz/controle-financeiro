package com.mfinancas.api.domain.entity.usuario;

import com.mfinancas.api.domain.entity.abstractentity.AbstractEntity;
import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Usuario extends AbstractEntity {
    private String nome;
    private String email;
    private String senha;

    public Usuario(UsuarioDTO usuarioDTO) {
        super();
        this.nome = usuarioDTO.nome();
        this.email = usuarioDTO.email();
        this.senha = usuarioDTO.senha();
    }
}
