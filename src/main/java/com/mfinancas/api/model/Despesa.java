package com.mfinancas.api.model;

import com.mfinancas.api.dto.DespesaTO;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Despesa extends AbstractEntity {
    private String descricao;

    private BigDecimal valor;

    private LocalDate dataVencimento;

    private Boolean pago;

    private UUID categoriaFK;

    private UUID usuarioFK;

    public Despesa(DespesaTO despesaTO) {
        descricao = despesaTO.descricao();
        valor = despesaTO.valor();
        dataVencimento = despesaTO.dataVencimento();
        pago = despesaTO.pago();
        categoriaFK = despesaTO.categoriaFK();
        usuarioFK = despesaTO.usuarioFK();
    }

    public boolean isPago() {
        return Boolean.TRUE.equals(this.pago);
    }

    public boolean isAtrasado() {
        return !isPago() && this.dataVencimento.isBefore(LocalDate.now());
    }

    public boolean isEmDia() {
        return isPago() && !this.dataVencimento.isBefore(LocalDate.now());
    }

    public void updateDespesa(DespesaTO despesaTO) {
        this.descricao = despesaTO.descricao();
        this.valor = despesaTO.valor();
        this.dataVencimento = despesaTO.dataVencimento();
        this.pago = despesaTO.pago();
        this.categoriaFK = despesaTO.categoriaFK();
        this.usuarioFK = despesaTO.usuarioFK();
    }
}
