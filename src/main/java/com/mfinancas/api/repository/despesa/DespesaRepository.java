package com.mfinancas.api.repository.despesa;

import com.mfinancas.api.domain.entity.despesa.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    Despesa findByUuid(UUID uuid);

    void deleteByUuid(UUID uuidDespesa);
}
