package com.example.inspection.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.inspection.dto.response.ReceiptImportDTO;
import com.example.inspection.service.ImportExcelService;

@RestController
@RequestMapping("/api/customers")
public class ImportFileController {

    private final ImportExcelService importExcelService;

    public ImportFileController(ImportExcelService importExcelService) {

        this.importExcelService = importExcelService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF')")
    @PostMapping("/upload-excel")
    public ResponseEntity<?> uploadCustomerExcel(@RequestParam("file") MultipartFile file) {
        try {
            ReceiptImportDTO receipt = importExcelService.importFromExcel(file);
            return ResponseEntity.ok(receipt);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Lỗi đọc file Excel: " + e.getMessage());
        }
    }

}
