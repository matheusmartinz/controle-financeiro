package com.mfinancas.api.api.controller.categoria;

import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
import com.mfinancas.api.service.categoria.CategoriaService;
import jakarta.validation.Valid;
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
    public ResponseEntity<CategoriaDTO> createCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO, @PathVariable UUID uuidUsuario) {
        return ResponseEntity.ok(categoriaService.createCategoria(categoriaDTO, uuidUsuario));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> findAllCategoria() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @PutMapping("/edit/{uuidCategoria}")
    public ResponseEntity<CategoriaDTO> updateCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO, @PathVariable UUID uuidCategoria) {
        return ResponseEntity.ok(categoriaService.updateCategoria(categoriaDTO, uuidCategoria));
    }

    @DeleteMapping("/delete/{uuidCategoria}")
    public ResponseEntity<String> deleteCategoria(@PathVariable UUID uuidCategoria) {
        categoriaService.deleteCategoria(uuidCategoria);
        return ResponseEntity.ok().body("Deletado com sucesso.");
    }

}
