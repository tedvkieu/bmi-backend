package com.example.inspection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.inspection.service.DocumentGenerationService;

import java.io.File;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentGenerationService documentGenerationService;

    @GetMapping("/generate-inspection-report/{receiptId}")
    public ResponseEntity<?> generateInspectionReport(@PathVariable Long receiptId) {
        try {
            String filePath = documentGenerationService.generateInspectionReport(receiptId);

            File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to generate document");
            }

            Resource resource = new FileSystemResource(file);

            String fileName = "inspection_report_" + receiptId + ".docx";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating document: " + e.getMessage());
        }
    }

    @GetMapping("/generate-inspection-report-path/{receiptId}")
    public ResponseEntity<String> generateInspectionReportPath(@PathVariable Long receiptId) {
        try {
            String filePath = documentGenerationService.generateInspectionReport(receiptId);
            return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating document: " + e.getMessage());
        }
    }
}