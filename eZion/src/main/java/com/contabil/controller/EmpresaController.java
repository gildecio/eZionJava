package com.contabil.controller;

import com.contabil.model.Empresa;
import com.contabil.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<Empresa> createEmpresa(@RequestBody Empresa empresa) {
        Empresa created = empresaService.createEmpresa(empresa);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        List<Empresa> empresas = empresaService.getAllEmpresas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable Long id) {
        Optional<Empresa> empresa = empresaService.getEmpresaById(id);
        return empresa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<Empresa> getEmpresaByCnpj(@PathVariable String cnpj) {
        Optional<Empresa> empresa = empresaService.getEmpresaByCnpj(cnpj);
        return empresa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/inscricao-estadual/{inscricaoEstadual}")
    public ResponseEntity<Empresa> getEmpresaByInscricaoEstadual(@PathVariable String inscricaoEstadual) {
        Optional<Empresa> empresa = empresaService.getEmpresaByInscricaoEstadual(inscricaoEstadual);
        return empresa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<Empresa>> getEmpresasAtivas() {
        List<Empresa> empresas = empresaService.getEmpresasAtivas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/inativas")
    public ResponseEntity<List<Empresa>> getEmpresasInativas() {
        List<Empresa> empresas = empresaService.getEmpresasInativas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/regime/{regime}")
    public ResponseEntity<List<Empresa>> getEmpresasByRegime(@PathVariable String regime) {
        try {
            Empresa.Regime r = Empresa.Regime.valueOf(regime.toUpperCase());
            List<Empresa> empresas = empresaService.getEmpresasByRegime(r);
            return ResponseEntity.ok(empresas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tipo-contribuinte/{tipoContribuinte}")
    public ResponseEntity<List<Empresa>> getEmpresasByTipoContribuinte(@PathVariable String tipoContribuinte) {
        try {
            Empresa.TipoContribuinte tc = Empresa.TipoContribuinte.valueOf(tipoContribuinte.toUpperCase());
            List<Empresa> empresas = empresaService.getEmpresasByTipoContribuinte(tc);
            return ResponseEntity.ok(empresas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> updateEmpresa(@PathVariable Long id, @RequestBody Empresa empresaDetails) {
        try {
            Empresa updated = empresaService.updateEmpresa(id, empresaDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/desativar")
    public ResponseEntity<?> desativarEmpresa(@PathVariable Long id) {
        try {
            empresaService.desativarEmpresa(id);
            return ResponseEntity.ok().body("Empresa desativada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/ativar")
    public ResponseEntity<?> ativarEmpresa(@PathVariable Long id) {
        try {
            empresaService.ativarEmpresa(id);
            return ResponseEntity.ok().body("Empresa ativada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        try {
            empresaService.deleteEmpresa(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
