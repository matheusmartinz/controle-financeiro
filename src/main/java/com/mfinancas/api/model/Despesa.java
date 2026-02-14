package com.mfinancas.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Despesa extends AbstractEntity {
    private String descricao;

    private BigDecimal valor;

    private LocalDate dataVencimento;

    private Boolean pago;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private UUID usuarioFK;
}
