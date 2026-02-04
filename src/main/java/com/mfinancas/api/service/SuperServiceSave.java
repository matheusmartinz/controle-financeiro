package com.mfinancas.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class SuperServiceSave<T, R extends JpaRepository<T, UUID>> {

    private final R repository;

    public T save(T entity) {
        return repository.save(entity);
    }
}
