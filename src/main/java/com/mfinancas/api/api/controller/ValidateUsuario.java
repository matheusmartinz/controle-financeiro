//package com.mfinancas.api.controller;
//
//import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
//import com.mfinancas.api.exceptions.FailedConditional;
//import com.mfinancas.api.utils.ValidateEmail;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ValidateUsuario {
//
//    public void validateUsuarioTO(UsuarioDTO usuarioDTO) {
//        boolean passwordIsNull = usuarioDTO.senha() == null || usuarioDTO.senha().isBlank();
//        boolean emailIsNull = usuarioDTO.email() == null || usuarioDTO.email().isBlank();
//
//        if (passwordIsNull && emailIsNull) {
//            throw new FailedConditional("Obrigatório inserir todos os campos.");
//        }
//        if (passwordIsNull) {
//            throw new FailedConditional("Obrigatório informar senha.");
//        }
//        ValidateEmail.validaEmail(usuarioDTO.email());
//    }
//}
