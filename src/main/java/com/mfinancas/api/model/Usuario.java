package com.mfinancas.api.model;

import com.mfinancas.api.dto.UsuarioTO;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Usuario extends AbstractEntity {

    private String email;

    private String senha;

    public Usuario(UsuarioTO usuarioTO) {
        super();
        this.email = usuarioTO.email();
        this.senha = usuarioTO.senha();
    }
}
