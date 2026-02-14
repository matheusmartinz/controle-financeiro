package com.mfinancas.api.service;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidateCategoriaTO {

    @Autowired
    private final UsuarioRepository usuarioRepository;

    public void validarCategoriaRequest(CategoriaTO categoriaTO, UUID usuarioFK) {
        if (usuarioRepository.findById(usuarioFK).isEmpty()) {
            throw new IsNull("Usuário não encontrado");
        }
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
