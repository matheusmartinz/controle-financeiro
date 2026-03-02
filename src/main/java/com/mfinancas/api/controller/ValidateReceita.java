package com.mfinancas.api.controller;

import com.mfinancas.api.dto.ReceitaTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.UsuarioRepository;
import com.mfinancas.api.service.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ValidateReceita {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public void validateReceitaTO(ReceitaTO receitaTO) {
        if (usuarioRepository.findByUuid(receitaTO.usuarioFK()) == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        if (categoriaRepository.findByUuid(receitaTO.categoriaFK()) == null) {
            throw new IsNull("Categoria não encontrada.");
        }
        if (receitaTO.descricao() == null || receitaTO.descricao().isEmpty()) {
            throw new FailedConditional("Obrigatório informar a descricão.");
        }
        if (receitaTO.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new FailedConditional("O valor deve ser maior que zero.");
        }
        if (receitaTO.data().isAfter(LocalDate.now())) {
            throw new FailedConditional("Não pode inserir recebimentos futuros.");
        }
    }
}
