package com.mfinancas.api.dataprovider;

import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
import com.mfinancas.api.api.dto.receita.ReceitaDTO;
import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.service.receita.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class ReceitaDataProvider {

    @Autowired
    private ReceitaService receitaService;

    @Autowired
    private CategoriaDataProvider categoriaDataProvider;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    public ReceitaDTO createReceitaDataProvider() {
        UsuarioDTO usuario = usuarioDataProvider.createUsuarioTO();
        CategoriaDTO categoria = categoriaDataProvider.createCategoria("Trabalho");

        ReceitaDTO receitaDTO = new ReceitaDTO(UUID.randomUUID(), "Salário", BigDecimal.valueOf(500),
                LocalDate.now(), categoria.uuidCategoria(), usuario.uuid());

        return receitaService.createReceita(receitaDTO);
    }

    public ReceitaDTO createReceitaCustom(String descricao, BigDecimal valor, LocalDate data, String nomeCategoria) {
        UsuarioDTO usuario = usuarioDataProvider.createUsuarioTO();
        CategoriaDTO categoria = categoriaDataProvider.createCategoria(nomeCategoria);

        ReceitaDTO receitaDTO = new ReceitaDTO(UUID.randomUUID(), descricao, valor,
                data, categoria.uuidCategoria(), usuario.uuid());

        return receitaService.createReceita(receitaDTO);
    }
}
