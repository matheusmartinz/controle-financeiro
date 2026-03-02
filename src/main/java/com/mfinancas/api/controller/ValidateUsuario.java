package com.mfinancas.api.controller;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.utils.ValidateEmail;
import org.springframework.stereotype.Component;

@Component
public class ValidateUsuario {

    public void validateUsuarioTO(UsuarioTO usuarioTO) {
        boolean passwordIsNull = usuarioTO.senha() == null || usuarioTO.senha().isBlank();
        boolean emailIsNull = usuarioTO.email() == null || usuarioTO.email().isBlank();

        if (passwordIsNull && emailIsNull) {
            throw new FailedConditional("Obrigatório inserir todos os campos.");
        }
        if (passwordIsNull) {
            throw new FailedConditional("Obrigatório informar senha.");
        }
        ValidateEmail.validaEmail(usuarioTO.email());
    }
}
