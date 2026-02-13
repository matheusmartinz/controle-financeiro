package com.mfinancas.api.service;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import org.springframework.stereotype.Component;

@Component
public class ValidateCategoriaTO {

    public void validarCategoriaRequest(CategoriaTO categoriaTO) {
        if (categoriaTO == null) {
            throw new IsNull("Obrigatório inserir as informações da categoria.");
        }
        if (categoriaTO.descricao().isEmpty()) {
            throw new FailedConditional("Obrigatório informar o descricao do categoriaTO.");
        }
        if (categoriaTO.tipo() == null) {
            throw new FailedConditional("Obrigatório informar o  tipo do categoriaTO.");
        }
    }
}
