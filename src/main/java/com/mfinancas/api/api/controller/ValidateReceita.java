//package com.mfinancas.api.api.controller;
//
//import com.mfinancas.api.api.dto.receita.ReceitaDTO;
//import com.mfinancas.api.exceptions.FailedConditional;
//import com.mfinancas.api.exceptions.IsNull;
//import com.mfinancas.api.repository.categoria.CategoriaRepository;
//import com.mfinancas.api.repository.usuario.UsuarioRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Component
//public class ValidateReceita {
//
//    @Autowired
//    private UsuarioRepository usuarioRepository;
//
//    @Autowired
//    private CategoriaRepository categoriaRepository;
//
//    public void validateReceitaTO(ReceitaDTO receitaDTO) {
//        if (usuarioRepository.findByUuid(receitaDTO.usuarioFK()) == null) {
//            throw new IsNull("Usuário não encontrado.");
//        }
//        if (categoriaRepository.findByUuid(receitaDTO.categoriaFK()) == null) {
//            throw new IsNull("Categoria não encontrada.");
//        }
//        if (receitaDTO.descricao() == null || receitaDTO.descricao().isEmpty()) {
//            throw new FailedConditional("Obrigatório informar a descricão.");
//        }
//        if (receitaDTO.valor().compareTo(BigDecimal.ZERO) <= 0) {
//            throw new FailedConditional("O valor deve ser maior que zero.");
//        }
//        if (receitaDTO.data().isAfter(LocalDate.now())) {
//            throw new FailedConditional("Não pode inserir recebimentos futuros.");
//        }
//    }
//}
