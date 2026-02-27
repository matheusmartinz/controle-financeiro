package com.mfinancas.api.service;

import com.mfinancas.api.controller.ValidateDespesa;
import com.mfinancas.api.dto.DespesaTO;
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

    public DespesaTO createDespesa(DespesaTO despesaTO) {
        validateDespesa.validaDespesaTO(despesaTO);

        Despesa despesa = new Despesa(despesaTO);

        return new DespesaTO(save(despesa));
    }

    public List<DespesaTO> findAllDespesas() {
        List<Despesa> despesas = despesaRepository.findAll();
        return despesas.stream().map(DespesaTO::new).toList();
    }

    public DespesaTO updateDespesa(DespesaTO despesaTO, UUID uuidDespesa) {
        Despesa entity = despesaRepository.findByUuid(uuidDespesa);

        if (usuarioRepository.findByUuid(despesaTO.usuarioFK()) == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        if (despesaRepository.findByUuid(uuidDespesa) == null) {
            throw new IsNull("Despesa não encontrada.");
        }

        entity.updateDespesa(despesaTO);

        return new DespesaTO(despesaRepository.save(entity));
    }
}
