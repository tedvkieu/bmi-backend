// package com.example.inspection.controller;

// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.inspection.dto.request.ExecutionUnitRequest;
// import com.example.inspection.dto.request.ExecutionUnitResponse;
// import com.example.inspection.service.ExecutionUnitService;

// import java.util.List;

// @RestController
// @RequestMapping("/api/execution-units")
// @RequiredArgsConstructor
// public class ExecutionUnitController {

// private final ExecutionUnitService executionUnitService;

// @PostMapping
// public ResponseEntity<ExecutionUnitResponse> create(@RequestBody
// ExecutionUnitRequest request) {
// return ResponseEntity.ok(executionUnitService.create(request));
// }

// @GetMapping("/{id}")
// public ResponseEntity<ExecutionUnitResponse> getById(@PathVariable Long id) {
// return ResponseEntity.ok(executionUnitService.getById(id));
// }

// @GetMapping
// public ResponseEntity<List<ExecutionUnitResponse>> getAll() {
// return ResponseEntity.ok(executionUnitService.getAll());
// }

// @PutMapping("/{id}")
// public ResponseEntity<ExecutionUnitResponse> update(@PathVariable Long id,
// @RequestBody ExecutionUnitRequest request) {
// return ResponseEntity.ok(executionUnitService.update(id, request));
// }

// @DeleteMapping("/{id}")
// public ResponseEntity<Void> delete(@PathVariable Long id) {
// executionUnitService.delete(id);
// return ResponseEntity.noContent().build();
// }
// }
