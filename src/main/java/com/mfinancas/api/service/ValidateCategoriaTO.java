package com.mfinancas.api.service;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidateCategoriaTO {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void validarCategoriaRequest(CategoriaTO categoriaTO, UUID uuidUsuario) {
        if (usuarioRepository.findById(uuidUsuario).isEmpty()) {
            throw new IsNull("Usuário não encontrado.");
        }
        if (categoriaTO.nome().isEmpty()) {
            throw new FailedConditional("Obrigatório informar o nome do categoria.");
        }
        if (categoriaTO.tipo() == null) {
            throw new FailedConditional("Obrigatório informar o  tipo do categoria.");
        }
    }
}
