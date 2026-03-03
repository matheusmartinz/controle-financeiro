package com.mfinancas.api.controller;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("categoria")
public class CategoriaRestController {
    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/create/{uuidUsuario}")
    public ResponseEntity<CategoriaTO> createCategoria(@RequestBody CategoriaTO categoriaTO, @PathVariable UUID uuidUsuario) {
        return ResponseEntity.ok(categoriaService.createCategoria(categoriaTO, uuidUsuario));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaTO>> findAllCategoria() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @PutMapping("/edit/{uuidCategoria}")
    public ResponseEntity<CategoriaTO> updateCategoria(@RequestBody CategoriaTO categoriaTO, @PathVariable UUID uuidCategoria) {
        return ResponseEntity.ok(categoriaService.updateCategoria(categoriaTO, uuidCategoria));
    }

    @DeleteMapping("/delete/{uuidCategoria}")
    public ResponseEntity<String> deleteCategoria(@PathVariable UUID uuidCategoria) {
        categoriaService.deleteCategoria(uuidCategoria);
        return ResponseEntity.ok().body("Deletado com sucesso.");
    }

}
