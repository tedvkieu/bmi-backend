package com.example.inspection.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.inspection.dto.request.MachineRequest;
import com.example.inspection.dto.response.MachineResponse;
import com.example.inspection.service.MachineService;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;

    @PostMapping
    public ResponseEntity<MachineResponse> create(@RequestBody MachineRequest request) {
        System.out.println("Creating Machine with request: " + request);
        return ResponseEntity.ok(machineService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(machineService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MachineResponse>> getAll() {
        return ResponseEntity.ok(machineService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MachineResponse> update(@PathVariable Long id,
            @RequestBody MachineRequest request) {
        return ResponseEntity.ok(machineService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        machineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
