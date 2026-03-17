package com.mfinancas.api.model;

import com.mfinancas.api.dto.CategoriaDTO;
import com.mfinancas.api.dto.UsuarioDTO;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Usuario extends AbstractEntity {
    private String nome;
    private String email;
    private String senha;
    private List<Categoria> categorias;
    private List<Receita> receitas;
    private List<Despesa> despesas;

    public Usuario(UsuarioDTO usuarioDTO) {
        super();
        this.nome = usuarioDTO.nome();
        this.email = usuarioDTO.email();
        this.senha = usuarioDTO.senha();
        this.categorias = UsuarioDTO.listOf(usuarioDTO.categorias());
    }
}
