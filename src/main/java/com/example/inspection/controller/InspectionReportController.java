package com.example.inspection.controller;

import com.example.inspection.dto.request.InspectionReportRequest;
import com.example.inspection.service.InspectionReportService;
import com.example.inspection.service.DocumentGenerationManager;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/api/inspection-reports")
@RequiredArgsConstructor
@Slf4j
public class InspectionReportController {

    private final InspectionReportService inspectionReportService;
    private final DocumentGenerationManager documentGenerationManager;

    // Executor cho async processing
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    /**
     * Generate inspection report PDF
     * POST /api/inspection-reports/generate-pdf
     */
    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generateInspectionReportPdf(
            @Valid @RequestBody InspectionReportRequest request) {

        Long startTime = System.currentTimeMillis();
        log.info("=== Starting PDF generation for receipt ID: {} ===", request.getReceiptId());

        try {
            // Validate input
            if (request.getReceiptId() == null || request.getReceiptId() <= 0) {
                log.error("Invalid receipt ID: {}", request.getReceiptId());
                return ResponseEntity.badRequest()
                        .body("Invalid receipt ID".getBytes());
            }

            // Check if can generate report
            if (!inspectionReportService.canGenerateReport(request.getReceiptId())) {
                log.error("Cannot generate report for receipt ID: {} - missing required data",
                        request.getReceiptId());
                return ResponseEntity.badRequest()
                        .body("Cannot generate report - missing required data".getBytes());
            }

            // Set default template if not provided
            String templateName = request.getTemplateName();
            if (templateName == null || templateName.trim().isEmpty()) {
                templateName = "inspection_report_template.docx";
                log.info("Using default template: {}", templateName);
            }

            log.info("Generating PDF with template: {}", templateName);

            // Generate PDF with timeout
            byte[] pdfBytes = generatePdfWithTimeout(request.getReceiptId(), templateName, 5); // 5 minutes timeout

            if (pdfBytes == null || pdfBytes.length == 0) {
                log.error("Generated PDF is empty for receipt ID: {}", request.getReceiptId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Generated PDF is empty".getBytes());
            }

            // Save file to resources/exports folder
            String fileName = "inspection_report_" + request.getReceiptId() + "_" + System.currentTimeMillis() + ".pdf";
            saveFileToResources(pdfBytes, fileName);

            // Create response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(pdfBytes.length);

            // Add cache control
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            Long duration = System.currentTimeMillis() - startTime;
            log.info("=== Successfully generated PDF for receipt ID: {} in {}ms ===",
                    request.getReceiptId(), duration);
            log.info("File saved to resources/exports: {}", fileName);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (TimeoutException e) {
            log.error("Timeout generating PDF for receipt ID: {}", request.getReceiptId(), e);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                    .body("PDF generation timed out - please try again later".getBytes());
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument for receipt ID: {}", request.getReceiptId(), e);
            return ResponseEntity.badRequest()
                    .body(("Invalid input: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            log.error("=== Error generating PDF for receipt ID: {} ===", request.getReceiptId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Generate inspection report PDF by receipt ID (GET method)
     */
    @GetMapping("/{receiptId}/pdf")
    public ResponseEntity<byte[]> generateInspectionReportPdfByGet(
            @PathVariable Long receiptId,
            @RequestParam(defaultValue = "inspection_report_template.docx") String templateName) {

        Long startTime = System.currentTimeMillis();
        log.info("=== Starting PDF generation (GET) for receipt ID: {} ===", receiptId);

        try {
            // Validate input
            if (receiptId == null || receiptId <= 0) {
                log.error("Invalid receipt ID: {}", receiptId);
                return ResponseEntity.badRequest()
                        .body("Invalid receipt ID".getBytes());
            }

            // Check if can generate report
            if (!inspectionReportService.canGenerateReport(receiptId)) {
                log.error("Cannot generate report for receipt ID: {} - missing required data", receiptId);
                return ResponseEntity.badRequest()
                        .body("Cannot generate report - missing required data".getBytes());
            }

            log.info("Generating PDF with template: {}", templateName);

            // Generate PDF with timeout
            byte[] pdfBytes = generatePdfWithTimeout(receiptId, templateName, 5); // 5 minutes timeout

            if (pdfBytes == null || pdfBytes.length == 0) {
                log.error("Generated PDF is empty for receipt ID: {}", receiptId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Generated PDF is empty".getBytes());
            }

            // Save file to resources/exports folder
            String fileName = "inspection_report_" + receiptId + "_" + System.currentTimeMillis() + ".pdf";
            saveFileToResources(pdfBytes, fileName);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(pdfBytes.length);

            // Add cache control
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            Long duration = System.currentTimeMillis() - startTime;
            log.info("=== Successfully generated PDF for receipt ID: {} in {}ms ===", receiptId, duration);
            log.info("File saved to resources/exports: {}", fileName);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (TimeoutException e) {
            log.error("Timeout generating PDF for receipt ID: {}", receiptId, e);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                    .body("PDF generation timed out - please try again later".getBytes());
        } catch (Exception e) {
            log.error("=== Error generating PDF for receipt ID: {} ===", receiptId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Preview inspection report data
     */
    @GetMapping("/{receiptId}/preview")
    public ResponseEntity<?> previewInspectionReportData(@PathVariable Long receiptId) {
        try {
            log.info("=== Previewing inspection report data for receipt ID: {} ===", receiptId);

            // Validate input
            if (receiptId == null || receiptId <= 0) {
                log.error("Invalid receipt ID: {}", receiptId);
                return ResponseEntity.badRequest()
                        .body("Invalid receipt ID");
            }

            var reportData = inspectionReportService.getInspectionReportDataWithValidation(receiptId);

            log.info("Successfully retrieved preview data for receipt ID: {}", receiptId);
            return ResponseEntity.ok(reportData);

        } catch (IllegalArgumentException e) {
            log.error("Invalid argument for receipt ID: {}", receiptId, e);
            return ResponseEntity.badRequest()
                    .body("Invalid input: " + e.getMessage());
        } catch (RuntimeException e) {
            log.error("Runtime error previewing report data for receipt ID: {}", receiptId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Receipt not found: " + e.getMessage());
        } catch (Exception e) {
            log.error("=== Error previewing report data for receipt ID: {} ===", receiptId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error previewing report data: " + e.getMessage());
        }
    }

    /**
     * Health check endpoint for PDF generation service
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        try {
            // Check LibreOffice availability through service
            boolean libreOfficeAvailable = inspectionReportService.isLibreOfficeAvailable();

            if (libreOfficeAvailable) {
                return ResponseEntity.ok().body(Map.of(
                        "status", "UP",
                        "libreOffice", "AVAILABLE",
                        "timestamp", System.currentTimeMillis()));
            } else {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(Map.of(
                                "status", "DOWN",
                                "libreOffice", "NOT_AVAILABLE",
                                "timestamp", System.currentTimeMillis()));
            }
        } catch (Exception e) {
            log.error("Health check failed", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "status", "DOWN",
                            "error", e.getMessage(),
                            "timestamp", System.currentTimeMillis()));
        }
    }

    /**
     * Generate PDF with timeout to prevent hanging
     */
    private byte[] generatePdfWithTimeout(Long receiptId, String templateName, int timeoutMinutes)
            throws TimeoutException, Exception {

        log.info("Starting PDF generation with {}min timeout for receipt ID: {}", timeoutMinutes, receiptId);

        Future<byte[]> future = executorService.submit(() -> {
            try {
                return inspectionReportService.generateInspectionReportPdf(receiptId, templateName);
            } catch (Exception e) {
                log.error("Error in PDF generation task for receipt ID: {}", receiptId, e);
                throw new RuntimeException("PDF generation failed: " + e.getMessage(), e);
            }
        });

        try {
            byte[] result = future.get(timeoutMinutes, TimeUnit.MINUTES);
            log.info("PDF generation completed successfully for receipt ID: {}", receiptId);
            return result;
        } catch (TimeoutException e) {
            future.cancel(true);
            log.error("PDF generation timed out after {} minutes for receipt ID: {}", timeoutMinutes, receiptId);
            throw e;
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                RuntimeException re = (RuntimeException) cause;
                if (re.getCause() instanceof Exception) {
                    throw (Exception) re.getCause();
                }
                throw re;
            }
            throw new Exception("PDF generation failed", cause);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.cancel(true);
            throw new Exception("PDF generation was interrupted", e);
        }
    }

    /**
     * Cleanup resources on shutdown
     */
    @PreDestroy
    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Test endpoint to verify service is working
     */
    @GetMapping("/test")
    public ResponseEntity<String> testService() {
        try {
            String result = inspectionReportService.testService();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Service test failed: " + e.getMessage());
        }
    }

    /**
     * Test all document generation services
     */
    @GetMapping("/test-all-services")
    public ResponseEntity<String> testAllServices() {
        try {
            String result = documentGenerationManager.testAllServices();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("All services test failed: " + e.getMessage());
        }
    }

    /**
     * Generate filled DOCX (no PDF conversion) - Fast endpoint
     * GET /api/inspection-reports/{receiptId}/docx
     */
    @GetMapping("/{receiptId}/docx")
    public ResponseEntity<byte[]> generateFilledDocx(
            @PathVariable Long receiptId,
            @RequestParam(defaultValue = "inspection_report_template.docx") String templateName) {

        Long startTime = System.currentTimeMillis();
        log.info("=== Starting DOCX generation (no PDF) for receipt ID: {} ===", receiptId);

        try {
            // Validate input
            if (receiptId == null || receiptId <= 0) {
                log.error("Invalid receipt ID: {}", receiptId);
                return ResponseEntity.badRequest()
                        .body("Invalid receipt ID".getBytes());
            }

            // Check if can generate report
            if (!inspectionReportService.canGenerateReport(receiptId)) {
                log.error("Cannot generate report for receipt ID: {} - missing required data", receiptId);
                return ResponseEntity.badRequest()
                        .body("Cannot generate report - missing required data".getBytes());
            }

            log.info("Generating DOCX with template: {}", templateName);

            // Generate DOCX with timeout (shorter timeout since no LibreOffice)
            byte[] docxBytes = generateDocxWithTimeout(receiptId, templateName, 2); // 2 minutes timeout

            if (docxBytes == null || docxBytes.length == 0) {
                log.error("Generated DOCX is empty for receipt ID: {}", receiptId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Generated DOCX is empty".getBytes());
            }

            // Save file to resources/exports folder
            String fileName = "inspection_report_" + receiptId + "_" + System.currentTimeMillis() + ".docx";
            saveFileToResources(docxBytes, fileName);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(docxBytes.length);

            // Add cache control
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            Long duration = System.currentTimeMillis() - startTime;
            log.info("=== Successfully generated DOCX for receipt ID: {} in {}ms ===", receiptId, duration);
            log.info("File saved to resources/exports: {}", fileName);

            return new ResponseEntity<>(docxBytes, headers, HttpStatus.OK);

        } catch (TimeoutException e) {
            log.error("Timeout generating DOCX for receipt ID: {}", receiptId, e);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                    .body("DOCX generation timed out - please try again later".getBytes());
        } catch (Exception e) {
            log.error("=== Error generating DOCX for receipt ID: {} ===", receiptId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating DOCX: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Generate filled DOCX (no PDF conversion) - Fast endpoint
     * POST /api/inspection-reports/generate-docx
     */
    @PostMapping("/generate-docx")
    public ResponseEntity<byte[]> generateFilledDocx(
            @Valid @RequestBody InspectionReportRequest request) {

        Long startTime = System.currentTimeMillis();
        log.info("=== Starting DOCX generation (no PDF) for receipt ID: {} ===", request.getReceiptId());

        try {
            // Validate input
            if (request.getReceiptId() == null || request.getReceiptId() <= 0) {
                log.error("Invalid receipt ID: {}", request.getReceiptId());
                return ResponseEntity.badRequest()
                        .body("Invalid receipt ID".getBytes());
            }

            // Check if can generate report
            if (!inspectionReportService.canGenerateReport(request.getReceiptId())) {
                log.error("Cannot generate report for receipt ID: {} - missing required data",
                        request.getReceiptId());
                return ResponseEntity.badRequest()
                        .body("Cannot generate report - missing required data".getBytes());
            }

            // Set default template if not provided
            String templateName = request.getTemplateName();
            if (templateName == null || templateName.trim().isEmpty()) {
                templateName = "inspection_report_template.docx";
                log.info("Using default template: {}", templateName);
            }

            log.info("Generating DOCX with template: {}", templateName);

            // Generate DOCX with timeout (shorter timeout since no LibreOffice)
            byte[] docxBytes = generateDocxWithTimeout(request.getReceiptId(), templateName, 2); // 2 minutes timeout

            if (docxBytes == null || docxBytes.length == 0) {
                log.error("Generated DOCX is empty for receipt ID: {}", request.getReceiptId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Generated DOCX is empty".getBytes());
            }

            // Save file to resources/exports folder
            String fileName = "inspection_report_" + request.getReceiptId() + "_" + System.currentTimeMillis()
                    + ".docx";
            saveFileToResources(docxBytes, fileName);

            // Create response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(docxBytes.length);

            // Add cache control
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            Long duration = System.currentTimeMillis() - startTime;
            log.info("=== Successfully generated DOCX for receipt ID: {} in {}ms ===",
                    request.getReceiptId(), duration);
            log.info("File saved to resources/exports: {}", fileName);

            return new ResponseEntity<>(docxBytes, headers, HttpStatus.OK);

        } catch (TimeoutException e) {
            log.error("Timeout generating DOCX for receipt ID: {}", request.getReceiptId(), e);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                    .body("DOCX generation timed out - please try again later".getBytes());
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument for receipt ID: {}", request.getReceiptId(), e);
            return ResponseEntity.badRequest()
                    .body(("Invalid input: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            log.error("=== Error generating DOCX for receipt ID: {} ===", request.getReceiptId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating DOCX: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Generate DOCX with timeout to prevent hanging (faster than PDF)
     */
    private byte[] generateDocxWithTimeout(Long receiptId, String templateName, int timeoutMinutes)
            throws TimeoutException, Exception {

        log.info("Starting DOCX generation with {}min timeout for receipt ID: {}", timeoutMinutes, receiptId);

        Future<byte[]> future = executorService.submit(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                try {
                    // Get data and generate DOCX using DocumentGenerationManager
                    var reportData = inspectionReportService.getInspectionReportDataWithValidation(receiptId);
                    Map<String, String> placeholders = inspectionReportService.convertToPlaceholders(reportData);
                    return documentGenerationManager.generateDocxFromTemplate(templateName, placeholders);
                } catch (Exception e) {
                    log.error("Error in DOCX generation task for receipt ID: {}", receiptId, e);
                    throw new RuntimeException("DOCX generation failed: " + e.getMessage(), e);
                }
            }
        });

        try {
            byte[] result = future.get(timeoutMinutes, TimeUnit.MINUTES);
            log.info("DOCX generation completed successfully for receipt ID: {}", receiptId);
            return result;
        } catch (TimeoutException e) {
            future.cancel(true);
            log.error("DOCX generation timed out after {} minutes for receipt ID: {}", timeoutMinutes, receiptId);
            throw e;
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                RuntimeException re = (RuntimeException) cause;
                if (re.getCause() instanceof Exception) {
                    throw (Exception) re.getCause();
                }
                throw re;
            }
            throw new Exception("DOCX generation failed", cause);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.cancel(true);
            throw new Exception("DOCX generation was interrupted", e);
        }
    }

    /**
     * Save file to resources/exports folder
     */
    private void saveFileToResources(byte[] fileBytes, String fileName) {
        try {
            // Create exports directory if it doesn't exist
            Path exportsDir = Paths.get("src/main/resources/exports");
            if (!Files.exists(exportsDir)) {
                Files.createDirectories(exportsDir);
                log.info("Created exports directory: {}", exportsDir);
            }

            // Save file to exports directory
            Path filePath = exportsDir.resolve(fileName);
            try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(fileBytes);
                fos.flush();
            }

            log.info("File saved successfully to: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to save file to resources/exports: {}", fileName, e);
            // Don't throw exception, just log the error
        }
    }
}