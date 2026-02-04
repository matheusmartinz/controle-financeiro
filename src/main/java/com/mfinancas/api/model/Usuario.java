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

    @OneToMany(mappedBy = "usuario")
    private List<Receita> receitas;

    @OneToMany(mappedBy = "usuario")
    private List<Despesa> despesas;

    @OneToMany(mappedBy = "usuario")
    private List<Categoria> categorias;

    public Usuario(UsuarioTO usuarioTO) {
        super();
        this.nome = usuarioTO.nome();
        this.email = usuarioTO.email();
        this.senha = usuarioTO.senha();
        this.receitas = usuarioTO.receitas();
        this.despesas = usuarioTO.despesas();
        this.categorias = usuarioTO.categorias();
    }
}
