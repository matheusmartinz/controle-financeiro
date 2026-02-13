package com.mfinancas.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ControleFinanceiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControleFinanceiroApplication.class, args);
    }
    //TODO Arrumar os testes de create Usuario - Não vou mais fazer Usuario ter as entidades Categoria / Despesa / Receita - Não faz sentido
}
