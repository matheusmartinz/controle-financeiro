package com.mfinancas.api.dataprovider;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.dto.ReceitaTO;
import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.service.ReceitaService;
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

    public ReceitaTO createReceitaDataProvider() {
        UsuarioTO usuario = usuarioDataProvider.createUsuarioTO();
        CategoriaTO categoria = categoriaDataProvider.createCategoria("Trabalho");

        ReceitaTO receitaTO = new ReceitaTO(UUID.randomUUID(), "Salário", BigDecimal.valueOf(500),
                LocalDate.now(), categoria.uuidCategoria(), usuario.uuid());

        return receitaService.createReceita(receitaTO);
    }

    public ReceitaTO createReceitaCustom(String descricao, BigDecimal valor, LocalDate data, String nomeCategoria) {
        UsuarioTO usuario = usuarioDataProvider.createUsuarioTO();
        CategoriaTO categoria = categoriaDataProvider.createCategoria(nomeCategoria);

        ReceitaTO receitaTO = new ReceitaTO(UUID.randomUUID(), descricao, valor,
                data, categoria.uuidCategoria(), usuario.uuid());

        return receitaService.createReceita(receitaTO);
    }
}
