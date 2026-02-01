package com.mfinancas.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ReceitaTO(String descricao, BigDecimal valor,
                        LocalDate data,
                        UUID usuario,
                        UUID categoria) {
}
