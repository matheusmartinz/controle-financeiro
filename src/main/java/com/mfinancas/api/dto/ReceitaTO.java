package com.mfinancas.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ReceitaTO(UUID uuidReceita,String descricao, BigDecimal valor,
                        LocalDate data,
                        UUID usuario,
                        CategoriaTO categoriaTO,
                        UUID usuarioFK) {
}
