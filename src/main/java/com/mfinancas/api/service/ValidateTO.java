package com.mfinancas.api.service;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.utils.ValidateEmail;
import org.springframework.stereotype.Component;

@Component
public class ValidateTO {

    public void validateUsuarioTO(UsuarioTO usuarioTO) {
        ValidateEmail.validaEmail(usuarioTO.email());
        if (usuarioTO.senha() == null || usuarioTO.senha().isBlank()) {
            throw new FailedConditional("Obrigatório informar senha.");
        }
    }
}
