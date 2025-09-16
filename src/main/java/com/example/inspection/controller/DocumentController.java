package com.example.inspection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.inspection.dto.response.ReceiptResponse;
import com.example.inspection.service.DocumentGenerationService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentGenerationService documentGenerationService;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF')")
    @GetMapping("/generate-inspection-report/{receiptId}")
    public ResponseEntity<?> generateInspectionReport(@PathVariable Long receiptId) {
        try {
            ReceiptResponse receipt = documentGenerationService.generateInspectionReport(receiptId);

            return ResponseEntity.ok(receipt);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating document: " + e.getMessage());
        }
    }

    // @GetMapping("/generate-inspection-report-path/{receiptId}")
    // public ResponseEntity<String> generateInspectionReportPath(@PathVariable Long
    // receiptId) {
    // try {
    // String filePath =
    // documentGenerationService.generateInspectionReport(receiptId);
    // return ResponseEntity.ok(filePath);
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body("Error generating document: " + e.getMessage());
    // }
    // }
}