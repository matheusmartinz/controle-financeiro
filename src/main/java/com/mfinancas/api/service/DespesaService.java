package com.mfinancas.api.service;

import com.mfinancas.api.controller.ValidateDespesa;
import com.mfinancas.api.dto.DespesaDTO;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.model.Despesa;
import com.mfinancas.api.repository.DespesaRepository;
import com.mfinancas.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DespesaService extends SuperServiceSave<Despesa, DespesaRepository> {
    public DespesaService(DespesaRepository repository) {
        super(repository);
    }

    @Autowired
    private ValidateDespesa validateDespesa;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public DespesaDTO createDespesa(DespesaDTO despesaDTO) {
        validateDespesa.validaDespesaTO(despesaDTO);

        Despesa despesa = new Despesa(despesaDTO);

        return new DespesaDTO(save(despesa));
    }

    public List<DespesaDTO> findAllDespesas() {
        List<Despesa> despesas = despesaRepository.findAll();
        return despesas.stream().map(DespesaDTO::new).toList();
    }

    public DespesaDTO updateDespesa(DespesaDTO despesaDTO, UUID uuidDespesa) {
        Despesa entity = despesaRepository.findByUuid(uuidDespesa);
        if (entity == null) {
            throw new IsNull("Despesa não encontrada.");
        }
        validateDespesa.validaDespesaTO(despesaDTO);
        entity.updateDespesa(despesaDTO);
        return new DespesaDTO(despesaRepository.save(entity));
    }

    public void deleteDespesa(UUID uuidDespesa) {
        if(despesaRepository.findByUuid(uuidDespesa) == null){
            throw new IsNull("Despesa não encontrada.");
        }
        despesaRepository.deleteByUuid(uuidDespesa);
    }
}
