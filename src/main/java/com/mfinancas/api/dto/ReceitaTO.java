package com.mfinancas.api.dto;

import com.mfinancas.api.model.Receita;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ReceitaTO(UUID uuidReceita, String descricao, BigDecimal valor,
                        LocalDate data,
                        UUID categoriaFK,
                        UUID usuarioFK) {

    public ReceitaTO(Receita receita) {
        this(receita.getUuid(), receita.getDescricao(),
                receita.getValor(), receita.getData(),
                receita.getCategoriaFK(), receita.getUsuarioFK());
    }

}
