package com.mfinancas.api.model;

import com.mfinancas.api.dto.ReceitaTO;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Receita extends AbstractEntity {

    private String descricao;

    private BigDecimal valor;

    private LocalDate data;

    private UUID categoriaFK;

    private UUID usuarioFK;

    public Receita(ReceitaTO receitaTO) {
        this.descricao = receitaTO.descricao();
        this.valor = receitaTO.valor();
        this.data = receitaTO.data();
        this.categoriaFK = receitaTO.categoriaFK();
        this.usuarioFK = receitaTO.usuarioFK();
    }

    public void atualizarReceita(String descricao, BigDecimal valor, LocalDate data, UUID categoriaFK) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoriaFK = categoriaFK;
    }
}
