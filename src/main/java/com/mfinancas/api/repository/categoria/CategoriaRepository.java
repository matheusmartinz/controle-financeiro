package com.mfinancas.api.repository.categoria;

import com.mfinancas.api.domain.entity.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNome(String nome);

    Categoria findByUuid(UUID uuid);

    void deleteByUuid(UUID uuidCategoria);
}
