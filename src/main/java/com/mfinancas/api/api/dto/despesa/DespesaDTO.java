package com.mfinancas.api.api.dto.despesa;

import com.mfinancas.api.domain.entity.despesa.Despesa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DespesaDTO(
        UUID uuidDespesa,
        @NotBlank(message = "Obrigatório informar a descrição.") String descricao,
        @PositiveOrZero(message = "O valor não pode ser menor do que zero.") BigDecimal valor,
        LocalDate dataVencimento,
        @NotNull Boolean pago,
        @NotNull UUID categoriaFK,
        @NotNull UUID usuarioFK
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
