package com.mfinancas.api.categoria;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.dataprovider.UsuarioCreateDataProvider;
import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.service.CategoriaRepository;
import com.mfinancas.api.service.CategoriaService;
import com.mfinancas.api.service.UsuarioService;
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
    UsuarioCreateDataProvider usuarioCreateDataProvider;
    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void createCategoria() {
        UsuarioTO usuarioTO = usuarioCreateDataProvider.createUsuarioTO();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(usuarioTO.categorias()).isNull();
        });

        CategoriaTO categoriaTO = new CategoriaTO(
                UUID.randomUUID(),
                "Lazer",
                TipoCategoria.DESPESA
        );
        long before = categoriaRepository.count();

        CategoriaTO toResponseCategoria = categoriaService.createCategoria(categoriaTO, usuarioTO.uuidUsuario());

        long after = categoriaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(toResponseCategoria.nome()).isNotNull();
            s.assertThat(toResponseCategoria.tipo()).isEqualTo(TipoCategoria.DESPESA);
            s.assertThat(before).isEqualTo(after + 1);
        });
    }
}
