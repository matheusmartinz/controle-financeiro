package com.mfinancas.api.controller;

import com.mfinancas.api.dto.DespesaDTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.UsuarioRepository;
import com.mfinancas.api.service.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidateDespesa {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public void validaDespesaTO(DespesaDTO despesaDTO) {

        if (usuarioRepository.findByUuid(despesaDTO.usuarioFK()) == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        if (despesaDTO.descricao() == null || despesaDTO.descricao().isEmpty()) {
            throw new FailedConditional("Favor informar a descrição.");
        }
        if (despesaDTO.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new FailedConditional("O valor deve ser maior do que zero.");
        }
        if (categoriaRepository.findByUuid(despesaDTO.categoriaFK()) == null) {
            throw new IsNull("Categoria não encontrada.");
        }
    }
}
