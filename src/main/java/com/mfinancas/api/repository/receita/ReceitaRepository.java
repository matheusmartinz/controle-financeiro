package com.mfinancas.api.repository.receita;

import com.mfinancas.api.domain.entity.receita.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    Receita findByUuid(UUID uuid);

    void deleteByUuid(UUID uuidReceita);
}
