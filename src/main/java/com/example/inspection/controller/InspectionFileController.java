// package com.example.inspection.controller;

// import com.example.inspection.dto.request.InspectionFileRequest;
// import com.example.inspection.dto.response.InspectionFileResponse;
// import com.example.inspection.service.InspectionFileService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.domain.Page;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/inspection-files")
// @RequiredArgsConstructor
// public class InspectionFileController {

// private final InspectionFileService inspectionFileService;

// @PostMapping
// public ResponseEntity<InspectionFileResponse> create(@RequestBody
// InspectionFileRequest request) {
// return ResponseEntity.ok(inspectionFileService.create(request));
// }

// @GetMapping
// public ResponseEntity<Page<InspectionFileResponse>> getAll(
// @RequestParam(defaultValue = "0") int page,
// @RequestParam(defaultValue = "10") int size) {
// return ResponseEntity.ok(inspectionFileService.getAll(page, size));
// }

// @GetMapping("/{id}")
// public ResponseEntity<InspectionFileResponse> getById(@PathVariable Long id)
// {
// return ResponseEntity.ok(inspectionFileService.getById(id));
// }

// @PutMapping("/{id}")
// public ResponseEntity<InspectionFileResponse> update(
// @PathVariable Long id,
// @RequestBody InspectionFileRequest request) {
// return ResponseEntity.ok(inspectionFileService.update(id, request));
// }

// @DeleteMapping("/{id}")
// public ResponseEntity<Void> delete(@PathVariable Long id) {
// inspectionFileService.delete(id);
// return ResponseEntity.noContent().build();
// }
// }
