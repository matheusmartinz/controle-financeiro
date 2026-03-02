package com.mfinancas.api.repository;

import com.mfinancas.api.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    Receita findByUuid(UUID uuid);
}
