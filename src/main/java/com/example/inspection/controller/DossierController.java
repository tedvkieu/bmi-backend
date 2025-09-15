package com.example.inspection.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inspection.dto.request.ReceiptRequest;
import com.example.inspection.dto.response.ReceiptResponse;
import com.example.inspection.service.DossierService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dossiers")
@RequiredArgsConstructor
public class DossierController {

    private final DossierService dossierService;

    @PostMapping
    public ResponseEntity<ReceiptResponse> create(@RequestBody ReceiptRequest request) {
        return ResponseEntity.ok(dossierService.createDossier(request));
    }

    @GetMapping
    public ResponseEntity<Page<ReceiptResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dossierService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dossierService.getDossierById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceiptResponse> update(
            @PathVariable Long id,
            @RequestBody ReceiptRequest request) {
        return ResponseEntity.ok(dossierService.updateDossier(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dossierService.deleteDossier(id);
        return ResponseEntity.noContent().build();
    }
}
