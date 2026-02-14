package com.mfinancas.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DespesaTO(
        UUID uuidDespesa,
        String descricao,
        BigDecimal valor,
        LocalDate dataVencimento,
        Boolean pago,
        CategoriaTO categoria,
        UUID usuarioFK) {
}
