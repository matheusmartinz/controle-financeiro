package com.mfinancas.api.api.controller.despesa;

import com.mfinancas.api.api.dto.despesa.DespesaDTO;
import com.mfinancas.api.service.despesa.DespesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("despesa")
public class DespesaRestController {

    @Autowired
    private DespesaService despesaService;

    @GetMapping
    public ResponseEntity<List<DespesaDTO>> getAllDespesas(){
        return ResponseEntity.ok(despesaService.findAllDespesas());
    }

    @PostMapping("/cadastro")
    public ResponseEntity<DespesaDTO> createDespesa(@Valid @RequestBody DespesaDTO despesaDTO){
        return ResponseEntity.ok(despesaService.createDespesa(despesaDTO));
    }

    @PutMapping("/editar/{uuidDespesa}")
    public ResponseEntity<DespesaDTO> updateDespesa(@Valid @RequestBody DespesaDTO despesaDTO, @PathVariable UUID uuidDespesa){
        return ResponseEntity.ok(despesaService.updateDespesa(despesaDTO, uuidDespesa));
    }

    @DeleteMapping("/delete/{uuidDespesa}")
    public ResponseEntity<String> deleteDespesa(@PathVariable UUID uuidDespesa){
        despesaService.deleteDespesa(uuidDespesa);
        return ResponseEntity.ok().body("Deletado com sucesso.");
    }
}
