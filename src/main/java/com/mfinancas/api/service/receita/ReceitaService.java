package com.mfinancas.api.service.receita;

import com.mfinancas.api.api.dto.receita.ReceitaDTO;
import com.mfinancas.api.domain.entity.receita.Receita;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.categoria.CategoriaRepository;
import com.mfinancas.api.repository.receita.ReceitaRepository;
import com.mfinancas.api.repository.usuario.UsuarioRepository;
import com.mfinancas.api.service.superservice.SuperServiceSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReceitaService extends SuperServiceSave<Receita, ReceitaRepository> {

    private ReceitaService(ReceitaRepository repository) {
        super(repository);
    }


    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ReceitaDTO createReceita(ReceitaDTO receitaDTO) {
        if (categoriaRepository.findByUuid(receitaDTO.categoriaFK()) == null) {
            throw new IsNull("Categoria não encontrada.");
        }
        if (usuarioRepository.findByUuid(receitaDTO.usuarioFK()) == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        if (receitaDTO.data().isAfter(LocalDate.now())) {
            throw new FailedConditional("Não pode inserir recebimentos futuros.");
        }
        Receita receita = new Receita(receitaDTO);
        return new ReceitaDTO(save(receita));
    }

    public List<ReceitaDTO> findAllReceitas() {
        return receitaRepository.findAll().stream().map(ReceitaDTO::new).toList();
    }

    public ReceitaDTO updateReceita(ReceitaDTO receitaDTO, UUID uuidReceita) {
        Receita receita = receitaRepository.findByUuid(uuidReceita);
        if (receita == null) {
            throw new IsNull("Receita não encontrada.");
        }
        receita.atualizarReceita(receitaDTO.descricao(), receitaDTO.valor(), receitaDTO.data(), receitaDTO.categoriaFK());
        return new ReceitaDTO(receitaRepository.save(receita));
    }

    public void deleteReceita(UUID uuidReceita) {
        Receita receita = receitaRepository.findByUuid(uuidReceita);
        if (receita == null) {
            throw new IsNull("Receita não encontrada.");
        }
        receitaRepository.deleteByUuid(uuidReceita);
    }
}
