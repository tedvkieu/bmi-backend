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

import com.example.inspection.dto.request.evaluation.InspectionCompletionEvaluationRequest;
import com.example.inspection.dto.response.evaluation.InspectionCompletionEvaluationResponse;
import com.example.inspection.service.evaluation.InspectionCompletionEvaluationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class InspectionCompletionEvaluationController {

    private final InspectionCompletionEvaluationService service;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF')")
    @PostMapping
    public ResponseEntity<InspectionCompletionEvaluationResponse> create(
            @RequestBody InspectionCompletionEvaluationRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<InspectionCompletionEvaluationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping("/all")
    public ResponseEntity<List<InspectionCompletionEvaluationResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping
    public ResponseEntity<Page<InspectionCompletionEvaluationResponse>> getAllForPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllForPage(page, size));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<InspectionCompletionEvaluationResponse> update(@PathVariable Long id,
            @RequestBody InspectionCompletionEvaluationRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping("/by-dossier/{dossierId}")
    public ResponseEntity<List<InspectionCompletionEvaluationResponse>> getByDossier(@PathVariable Long dossierId) {
        return ResponseEntity.ok(service.getByDossierId(dossierId));
    }
}

