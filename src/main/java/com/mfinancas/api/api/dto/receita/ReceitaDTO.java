package com.mfinancas.api.api.dto.receita;

import com.mfinancas.api.domain.entity.receita.Receita;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ReceitaDTO(UUID uuidReceita,
                         @NotBlank(message = "Obrigatório informar a descrição.") String descricao,
                         @PositiveOrZero(message = "O valor não pode ser menor do que zero.") BigDecimal valor,
                         @NotNull(message = "Obrigatório inserir data.") LocalDate data,
                         @NotNull UUID categoriaFK,
                         @NotNull UUID usuarioFK) {

    public ReceitaDTO(Receita receita) {
        this(receita.getUuid(), receita.getDescricao(), receita.getValor(),
                receita.getData(), receita.getCategoriaFK(), receita.getUsuarioFK());
    }

}
