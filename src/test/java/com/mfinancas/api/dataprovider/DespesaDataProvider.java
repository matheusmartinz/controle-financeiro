package com.mfinancas.api.dataprovider;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.dto.DespesaTO;
import com.mfinancas.api.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class DespesaDataProvider {

    @Autowired
    private DespesaService despesaService;

    @Autowired
    private CategoriaDataProvider categoriaDataProvider;


    public DespesaTO createDespesa() {
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Lazer");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(),
                "Beto Carreiro", BigDecimal.valueOf(280.9),
                LocalDate.now(), false, categoriaTO.uuidCategoria(), categoriaTO.usuarioFK());

        return despesaService.createDespesa(despesaTO);
    }

    public DespesaTO createDespesaCustom(String nomeCategoria, String nomeDespesa){
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria(nomeCategoria);

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(),
                nomeDespesa, BigDecimal.valueOf(280.9),
                LocalDate.now(), false, categoriaTO.uuidCategoria(), categoriaTO.usuarioFK());

        return despesaService.createDespesa(despesaTO);
    }
}
