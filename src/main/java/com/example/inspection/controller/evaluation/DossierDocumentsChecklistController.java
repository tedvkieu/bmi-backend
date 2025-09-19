package com.example.inspection.controller.evaluation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inspection.dto.request.evaluation.DossierDocumentsChecklistRequest;
import com.example.inspection.dto.response.evaluation.DossierDocumentsChecklistResponse;
import com.example.inspection.service.evaluation.DossierDocumentsChecklistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluations/checklist")
@RequiredArgsConstructor
public class DossierDocumentsChecklistController {

    private final DossierDocumentsChecklistService service;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF')")
    @PostMapping
    public ResponseEntity<DossierDocumentsChecklistResponse> create(@RequestBody DossierDocumentsChecklistRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DossierDocumentsChecklistResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping("/all")
    public ResponseEntity<List<DossierDocumentsChecklistResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping
    public ResponseEntity<Page<DossierDocumentsChecklistResponse>> getAllForPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllForPage(page, size));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<DossierDocumentsChecklistResponse> update(@PathVariable Long id,
            @RequestBody DossierDocumentsChecklistRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping("/by-evaluation/{evaluationId}")
    public ResponseEntity<List<DossierDocumentsChecklistResponse>> getByEvaluation(@PathVariable Long evaluationId) {
        return ResponseEntity.ok(service.getByEvaluationId(evaluationId));
    }
}

