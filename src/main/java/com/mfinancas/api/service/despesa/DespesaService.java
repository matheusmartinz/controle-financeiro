package com.mfinancas.api.service.despesa;

import com.mfinancas.api.api.dto.despesa.DespesaDTO;
import com.mfinancas.api.domain.entity.despesa.Despesa;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.categoria.CategoriaRepository;
import com.mfinancas.api.repository.despesa.DespesaRepository;
import com.mfinancas.api.repository.usuario.UsuarioRepository;
import com.mfinancas.api.service.superservice.SuperServiceSave;
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
    private DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public DespesaDTO createDespesa(DespesaDTO despesaDTO) {
        if (usuarioRepository.findByUuid(despesaDTO.usuarioFK()) == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        if (categoriaRepository.findByUuid(despesaDTO.categoriaFK()) == null) {
            throw new IsNull("Categoria não encontrada.");
        }
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
        entity.updateDespesa(despesaDTO);
        return new DespesaDTO(despesaRepository.save(entity));
    }

    public void deleteDespesa(UUID uuidDespesa) {
        if (despesaRepository.findByUuid(uuidDespesa) == null) {
            throw new IsNull("Despesa não encontrada.");
        }
        despesaRepository.deleteByUuid(uuidDespesa);
    }
}
