//package com.mfinancas.api.api.controller;
//
//import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
//import com.mfinancas.api.exceptions.FailedConditional;
//import com.mfinancas.api.exceptions.IsNull;
//import com.mfinancas.api.repository.usuario.UsuarioRepository;
//import com.mfinancas.api.repository.categoria.CategoriaRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class ValidateCategoria {
//
//    @Autowired
//    private final UsuarioRepository usuarioRepository;
//
//    @Autowired
//    private final CategoriaRepository categoriaRepository;
//
//    public void validarCategoriaRequest(CategoriaDTO categoriaDTO, UUID uuidUsuario) {
//        if (usuarioRepository.findByUuid(uuidUsuario) == null) {
//            throw new IsNull("Usuário não encontrado.");
//        }
//        if (categoriaRepository.existsByNome(categoriaDTO.nome())) {
//            throw new FailedConditional("Já existe um nome cadastrado com essa categoria.");
//        }
//        if (categoriaDTO.nome().isEmpty()) {
//            throw new FailedConditional("Obrigatório informar o descricao da categoria.");
//        }
//        if (categoriaDTO.tipo() == null) {
//            throw new FailedConditional("Obrigatório informar o  tipo da categoria.");
//        }
//    }
//}
