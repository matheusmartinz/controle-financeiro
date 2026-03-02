package com.mfinancas.api.service;

import com.mfinancas.api.controller.ValidateReceita;
import com.mfinancas.api.dto.ReceitaTO;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.model.Receita;
import com.mfinancas.api.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReceitaService extends SuperServiceSave<Receita, ReceitaRepository> {

    private ReceitaService(ReceitaRepository repository) {
        super(repository);
    }

    @Autowired
    private ValidateReceita validator;

    @Autowired
    private ReceitaRepository receitaRepository;

    public ReceitaTO createReceita(ReceitaTO receitaTO) {
        validator.validateReceitaTO(receitaTO);
        Receita receita = new Receita(receitaTO);
        return new ReceitaTO(save(receita));
    }

    public List<ReceitaTO> findAllReceitas() {
        return receitaRepository.findAll().stream().map(ReceitaTO::new).toList();
    }

    public ReceitaTO updateReceita(ReceitaTO receitaTO, UUID uuidReceita) {
        Receita receita = receitaRepository.findByUuid(uuidReceita);
        if (receita == null){
            throw new IsNull("Receita não encontrada.");
        }
        receita.atualizarReceita(receitaTO.descricao(), receitaTO.valor(), receitaTO.data(), receitaTO.categoriaFK());
        return new ReceitaTO(receitaRepository.save(receita));
    }
}
