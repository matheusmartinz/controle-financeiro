package com.mfinancas.api.model;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.dto.CategoriaTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Categoria extends AbstractEntity {
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoCategoria tipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Categoria(CategoriaTO categoriaTO, Usuario usuario) {
        super();
        this.nome = categoriaTO.nome();
        this.tipo = categoriaTO.tipo();
        this.usuario = usuario;
    }
}
