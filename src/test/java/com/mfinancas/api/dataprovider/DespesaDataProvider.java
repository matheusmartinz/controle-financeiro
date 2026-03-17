package com.mfinancas.api.dataprovider;

import com.mfinancas.api.dto.CategoriaDTO;
import com.mfinancas.api.dto.DespesaDTO;
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


    public DespesaDTO createDespesa() {
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Lazer");

        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(),
                "Beto Carreiro", BigDecimal.valueOf(280.9),
                LocalDate.now(), false, categoriaDTO.uuidCategoria(), categoriaDTO.usuarioFK());

        return despesaService.createDespesa(despesaDTO);
    }

    public DespesaDTO createDespesaCustom(String nomeCategoria, String nomeDespesa){
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria(nomeCategoria);

        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(),
                nomeDespesa, BigDecimal.valueOf(280.9),
                LocalDate.now(), false, categoriaDTO.uuidCategoria(), categoriaDTO.usuarioFK());

        return despesaService.createDespesa(despesaDTO);
    }
}
