package com.mfinancas.api.service;

import com.mfinancas.api.controller.ValidateReceita;
import com.mfinancas.api.dto.ReceitaDTO;
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

    public ReceitaDTO createReceita(ReceitaDTO receitaDTO) {
        validator.validateReceitaTO(receitaDTO);
        Receita receita = new Receita(receitaDTO);
        return new ReceitaDTO(save(receita));
    }

    public List<ReceitaDTO> findAllReceitas() {
        return receitaRepository.findAll().stream().map(ReceitaDTO::new).toList();
    }

    public ReceitaDTO updateReceita(ReceitaDTO receitaDTO, UUID uuidReceita) {
        Receita receita = receitaRepository.findByUuid(uuidReceita);
        if (receita == null){
            throw new IsNull("Receita não encontrada.");
        }
        receita.atualizarReceita(receitaDTO.descricao(), receitaDTO.valor(), receitaDTO.data(), receitaDTO.categoriaFK());
        return new ReceitaDTO(receitaRepository.save(receita));
    }

    public void deleteReceita(UUID uuidReceita) {
        Receita receita = receitaRepository.findByUuid(uuidReceita);
        if (receita == null){
            throw new IsNull("Receita não encontrada.");
        }
        receitaRepository.deleteByUuid(uuidReceita);
    }
}
