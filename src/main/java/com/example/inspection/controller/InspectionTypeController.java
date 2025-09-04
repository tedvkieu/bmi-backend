package com.example.inspection.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<InspectionTypeResponse> create(@RequestBody InspectionTypeRequest request) {
        return ResponseEntity.ok(inspectionTypeService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionTypeResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(inspectionTypeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<InspectionTypeResponse>> getAll() {
        return ResponseEntity.ok(inspectionTypeService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InspectionTypeResponse> update(@PathVariable String id,
            @RequestBody InspectionTypeRequest request) {
        return ResponseEntity.ok(inspectionTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        inspectionTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
