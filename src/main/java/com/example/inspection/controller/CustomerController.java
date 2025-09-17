package com.example.inspection.controller;

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

import com.example.inspection.dto.request.CustomerRequest;
import com.example.inspection.dto.response.CustomerResponse;
import com.example.inspection.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // Public contact submission (no auth)
    @PostMapping("/public")
    public ResponseEntity<CustomerResponse> publicContact(@RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF')")
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping("/all")
    public ResponseEntity<List<CustomerResponse>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> getAllForPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(customerService.getAllForPage(page, size));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long id, @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.update(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
