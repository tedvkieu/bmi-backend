package com.example.inspection.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.inspection.dto.request.InspectionTypeRequest;
import com.example.inspection.dto.response.InspectionTypeResponse;
import com.example.inspection.service.InspectionTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/inspection-types")
@RequiredArgsConstructor
public class InspectionTypeController {

    private final InspectionTypeService inspectionTypeService;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF')")
    @PostMapping
    public ResponseEntity<InspectionTypeResponse> create(@RequestBody InspectionTypeRequest request) {
        return ResponseEntity.ok(inspectionTypeService.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<InspectionTypeResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(inspectionTypeService.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping
    public ResponseEntity<List<InspectionTypeResponse>> getAll() {
        return ResponseEntity.ok(inspectionTypeService.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<InspectionTypeResponse> update(@PathVariable String id,
            @RequestBody InspectionTypeRequest request) {
        return ResponseEntity.ok(inspectionTypeService.update(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        inspectionTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
