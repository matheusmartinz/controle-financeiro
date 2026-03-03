package com.mfinancas.api.controller;

import com.mfinancas.api.dto.ReceitaTO;
import com.mfinancas.api.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ReceitaTO> createReceita(@RequestBody ReceitaTO receitaTO){
        return ResponseEntity.ok(receitaService.createReceita(receitaTO));
    }

    @GetMapping
    public ResponseEntity<List<ReceitaTO>> getAllReceitas(){
        return ResponseEntity.ok(receitaService.findAllReceitas());
    }

    @PutMapping("/edit/{uuidReceita}")
    public ResponseEntity<ReceitaTO> updateReceita(@RequestBody ReceitaTO receitaTO, @PathVariable UUID uuidReceita){
        return ResponseEntity.ok(receitaService.updateReceita(receitaTO, uuidReceita));
    }

    @DeleteMapping("/delete/{uuidReceita}")
    public ResponseEntity<String> deleteReceita(@PathVariable UUID uuidReceita){
        receitaService.deleteReceita(uuidReceita);
        return ResponseEntity.ok().body("Receita deletada com sucesso.");
    }
}
