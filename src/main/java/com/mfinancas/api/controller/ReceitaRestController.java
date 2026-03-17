package com.mfinancas.api.controller;

import com.mfinancas.api.dto.ReceitaDTO;
import com.mfinancas.api.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("receita")
public class ReceitaRestController {

    @Autowired
    private ReceitaService receitaService;

    @PostMapping("/create")
    public ResponseEntity<ReceitaDTO> createReceita(@RequestBody ReceitaDTO receitaDTO){
        return ResponseEntity.ok(receitaService.createReceita(receitaDTO));
    }

    @GetMapping
    public ResponseEntity<List<ReceitaDTO>> getAllReceitas(){
        return ResponseEntity.ok(receitaService.findAllReceitas());
    }

    @PutMapping("/edit/{uuidReceita}")
    public ResponseEntity<ReceitaDTO> updateReceita(@RequestBody ReceitaDTO receitaDTO, @PathVariable UUID uuidReceita){
        return ResponseEntity.ok(receitaService.updateReceita(receitaDTO, uuidReceita));
    }

    @DeleteMapping("/delete/{uuidReceita}")
    public ResponseEntity<String> deleteReceita(@PathVariable UUID uuidReceita){
        receitaService.deleteReceita(uuidReceita);
        return ResponseEntity.ok().body("Receita deletada com sucesso.");
    }
}
