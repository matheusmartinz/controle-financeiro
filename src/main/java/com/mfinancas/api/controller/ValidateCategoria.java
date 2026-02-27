package com.mfinancas.api.controller;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.UsuarioRepository;
import com.mfinancas.api.service.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidateCategoria {

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final CategoriaRepository categoriaRepository;

    public void validarCategoriaRequest(CategoriaTO categoriaTO, UUID uuidUsuario) {
        if (usuarioRepository.findByUuid(uuidUsuario) == null) {
            throw new IsNull("Usuário não encontrado");
        }
        if (categoriaRepository.existsByNome(categoriaTO.nome())) {
            throw new FailedConditional("Já existe um nome cadastrado com essa categoria.");
        }
        if (categoriaTO == null) {
            throw new IsNull("Obrigatório inserir as informações da categoria.");
        }
        if (categoriaTO.nome().isEmpty()) {
            throw new FailedConditional("Obrigatório informar o descricao do categoriaTO.");
        }
        if (categoriaTO.tipo() == null) {
            throw new FailedConditional("Obrigatório informar o  tipo do categoriaTO.");
        }
    }
}
