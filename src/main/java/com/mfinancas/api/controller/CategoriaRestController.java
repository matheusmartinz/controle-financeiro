package com.mfinancas.api.controller;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categoria")
public class CategoriaRestController {
    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/create")
    public ResponseEntity<CategoriaTO> createCategoria(@RequestBody CategoriaTO categoriaTO) {
        return ResponseEntity.ok(categoriaService.createCategoria(categoriaTO));
    }

//    @GetMapping
//    public ResponseEntity<List<CategoriaTO>> findAll() {
//        return ResponseEntity.ok(categoriaService.getAllCategorias());
//    }
}
