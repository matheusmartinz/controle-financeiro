package com.mfinancas.api.model;

import com.mfinancas.api.dto.UsuarioTO;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Usuario extends AbstractEntity {

    private String nome;

    private String email;

    private String senha;


    public Usuario(UsuarioTO usuarioTO) {
        super();
        this.nome = usuarioTO.nome();
        this.email = usuarioTO.email();
        this.senha = usuarioTO.senha();
    }
}
