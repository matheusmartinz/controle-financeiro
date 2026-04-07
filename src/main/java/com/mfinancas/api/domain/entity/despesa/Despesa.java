package com.mfinancas.api.domain.entity.despesa;

import com.mfinancas.api.domain.entity.abstractentity.AbstractEntity;
import com.mfinancas.api.api.dto.despesa.DespesaDTO;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Despesa(DespesaDTO despesaDTO) {
        descricao = despesaDTO.descricao();
        valor = despesaDTO.valor();
        dataVencimento = despesaDTO.dataVencimento();
        pago = despesaDTO.pago();
        categoriaFK = despesaDTO.categoriaFK();
        usuarioFK = despesaDTO.usuarioFK();
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

    public void updateDespesa(DespesaDTO despesaDTO) {
        this.descricao = despesaDTO.descricao();
        this.valor = despesaDTO.valor();
        this.dataVencimento = despesaDTO.dataVencimento();
        this.pago = despesaDTO.pago();
        this.categoriaFK = despesaDTO.categoriaFK();
        this.usuarioFK = despesaDTO.usuarioFK();
    }
}
