package com.mfinancas.api.dto;

import com.mfinancas.api.model.Despesa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DespesaDTO(
        UUID uuidDespesa,
        String descricao,
        BigDecimal valor,
        LocalDate dataVencimento,
        Boolean pago,
        UUID categoriaFK,
        UUID usuarioFK
) {

    public DespesaDTO(Despesa despesa) {
        this(despesa.getUuid(),
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getDataVencimento(),
                despesa.getPago(),
                despesa.getCategoriaFK(),
                despesa.getUsuarioFK());
    }
}
