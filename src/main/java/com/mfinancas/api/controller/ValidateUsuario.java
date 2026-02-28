package com.mfinancas.api.controller;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.utils.ValidateEmail;
import org.springframework.stereotype.Component;

@Component
public class ValidateUsuario {

    public void validateUsuarioTO(UsuarioTO usuarioTO) {
        if ((usuarioTO.senha() == null || usuarioTO.senha().isBlank()) &&
                (usuarioTO.email() == null || usuarioTO.email().isBlank())) {
            throw new FailedConditional("Obrigatório inserir todos os campos.");
        }
        if (usuarioTO.senha() == null || usuarioTO.senha().isBlank()) {
            throw new FailedConditional("Obrigatório informar senha.");
        }
        ValidateEmail.validaEmail(usuarioTO.email());
    }
}
