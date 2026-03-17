package com.mfinancas.api.categoria;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.dataprovider.CategoriaDataProvider;
import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.dto.CategoriaDTO;
import com.mfinancas.api.dto.UsuarioDTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.model.Categoria;
import com.mfinancas.api.service.CategoriaRepository;
import com.mfinancas.api.service.CategoriaService;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class CategoriaServiceIT {
    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    UsuarioDataProvider usuarioDataProvider;

    @Autowired
    CategoriaDataProvider categoriaDataProvider;

    @Test
    public void createCategoria() {
        UsuarioDTO usuarioResponse = usuarioDataProvider.createUsuarioTO();

        CategoriaDTO categoriaDTO = new CategoriaDTO(
                UUID.randomUUID(),
                "Moto",
                TipoCategoria.DESPESA,
                usuarioResponse.uuid()
        );
        long before = categoriaRepository.count();

        CategoriaDTO toResponseCategoria = categoriaService.createCategoria(categoriaDTO, categoriaDTO.usuarioFK());

        long after = categoriaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(toResponseCategoria.nome()).isNotNull();
            s.assertThat(toResponseCategoria.tipo()).isEqualTo(TipoCategoria.DESPESA);
            s.assertThat(after).isEqualTo(before + 1);
        });
    }

    @Test
    @SneakyThrows
    public void categoriaNotFound() {
        CategoriaDTO categoriaDTO = new CategoriaDTO(UUID.randomUUID(), "Caminhão", TipoCategoria.DESPESA, null);


        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> categoriaService.createCategoria(categoriaDTO, categoriaDTO.usuarioFK()))
                    .isInstanceOf(IsNull.class)
                    .hasMessage("Usuário não encontrado.");
        });
    }

    @Test
    @SneakyThrows
    public void categoriaSameName() {
        UsuarioDTO usuarioDTO = usuarioDataProvider.createUsuarioTO();

        CategoriaDTO categoriaDTO = new CategoriaDTO(UUID.randomUUID(), "Caminhão", TipoCategoria.DESPESA, usuarioDTO.uuid());

        categoriaService.createCategoria(categoriaDTO, usuarioDTO.uuid());

        CategoriaDTO categoriaDTOSameName = new CategoriaDTO(UUID.randomUUID(), "Caminhão", TipoCategoria.DESPESA, usuarioDTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> categoriaService.createCategoria(categoriaDTOSameName, usuarioDTO.uuid()))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("Já existe um nome cadastrado com essa categoria.");
        });
    }

    @Test
    @SneakyThrows
    public void categoriaNomeVazio() {
        UsuarioDTO usuarioDTO = usuarioDataProvider.createUsuarioTO();

        CategoriaDTO categoriaDTO = new CategoriaDTO(UUID.randomUUID(), "", TipoCategoria.DESPESA, usuarioDTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> categoriaService.createCategoria(categoriaDTO, usuarioDTO.uuid()))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("Obrigatório informar o descricao da categoria.");
        });
    }

    @Test
    @SneakyThrows
    public void categoriaTipoNull() {
        UsuarioDTO usuarioDTO = usuarioDataProvider.createUsuarioTO();

        CategoriaDTO categoriaDTO = new CategoriaDTO(UUID.randomUUID(), "Grama", null, usuarioDTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> categoriaService.createCategoria(categoriaDTO, usuarioDTO.uuid()))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("Obrigatório informar o  tipo da categoria.");
        });
    }

    @Test
    public void updateCategoria() {
        categoriaRepository.deleteAll();
        UsuarioDTO usuarioDTO = usuarioDataProvider.createUsuarioTO();

        long before = categoriaRepository.count();
        CategoriaDTO categoriaResponse = categoriaDataProvider.createCategoria("Fazenda");
        Categoria categoriaSaved = categoriaRepository.findByUuid(categoriaResponse.uuidCategoria());
        long after = categoriaRepository.count();

        CategoriaDTO categoriaUpdate = new CategoriaDTO(categoriaSaved.getUuid(), "Fazendinha", TipoCategoria.DESPESA, usuarioDTO.uuid());
        long afterUpdate = categoriaRepository.count();
        CategoriaDTO categoriaUpdated = categoriaService.updateCategoria(categoriaUpdate, categoriaResponse.uuidCategoria());

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 1);
            s.assertThat(afterUpdate).isEqualTo(after);

            s.assertThat(categoriaUpdated.uuidCategoria()).isEqualTo(categoriaSaved.getUuid());

        });
    }

    @Test
    @SneakyThrows
    public void updateCategoriaIsNull() {
        usuarioDataProvider.createUsuarioTO();

        categoriaDataProvider.createCategoria("Fazenda2");

        CategoriaDTO categoriaUpdate = categoriaDataProvider.createCategoria("Fazendinha3");

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> categoriaService.updateCategoria(categoriaUpdate, UUID.randomUUID())).isInstanceOf(IsNull.class)
                    .hasMessage("Categoria não encontrada.");
        });
    }

    @Test
    @Transactional
    public void deleteCategoria() {
        categoriaRepository.deleteAll();
        CategoriaDTO categoriaSaved = categoriaDataProvider.createCategoria("TestedeDeletar");
        long afterCreated = categoriaRepository.count();

        categoriaService.deleteCategoria(categoriaSaved.uuidCategoria());
        long afterDeleted = categoriaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(afterCreated).isEqualTo(1);
            s.assertThat(afterDeleted).isEqualTo(0);
        });
    }

    @Test
    public void deleteCategoriaIsNull() {
        categoriaDataProvider.createCategoria("TesteDelete2");

        SoftAssertions.assertSoftly(s -> {

            s.assertThatThrownBy(() -> categoriaService.deleteCategoria(UUID.randomUUID())).isInstanceOf(IsNull.class)
                    .hasMessage("Categoria não encontrada.");
        });
    }
}
