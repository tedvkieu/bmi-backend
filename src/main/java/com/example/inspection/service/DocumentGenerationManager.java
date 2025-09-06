package com.example.inspection.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Document Generation Manager
 * Quản lý và chọn service phù hợp để generate PDF từ template DOCX
 * Có thể fallback giữa các service khác nhau nếu một service fail
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentGenerationManager {

    private final DocumentTemplateService documentTemplateService;
    private final ImprovedDocumentService improvedDocumentService;
    private final TemplateEngineDocumentService templateEngineDocumentService;
    private final DocxOnlyService docxOnlyService;

    @Value("${app.document.generation.strategy:improved}")
    private String generationStrategy;

    /**
     * Generate DOCX from template (fast, no PDF conversion)
     */
    public byte[] generateDocxFromTemplate(String templateName, Map<String, String> placeholders) throws Exception {
        log.info("=== Document Generation Manager - Starting DOCX generation ===");
        log.info("Strategy: docx-only, Template: {}", templateName);

        // For DOCX generation, always use DocxOnlyService (fastest)
        return docxOnlyService.generateFilledDocx(templateName, placeholders);
    }

    /**
     * Generate PDF from template với strategy được chọn
     */
    public byte[] generatePdfFromTemplate(String templateName, Map<String, String> placeholders) throws Exception {
        log.info("=== Document Generation Manager - Starting PDF generation ===");
        log.info("Strategy: {}, Template: {}", generationStrategy, templateName);

        // Try different strategies based on configuration
        switch (generationStrategy.toLowerCase()) {
            case "simple":
                return tryWithFallback("Simple",
                        () -> documentTemplateService.generatePdfFromTemplate(templateName, placeholders),
                        templateName, placeholders);

            case "improved":
                return tryWithFallback("Improved",
                        () -> improvedDocumentService.generatePdfFromTemplate(templateName, placeholders),
                        templateName, placeholders);

            case "template-engine":
                return tryWithFallback("Template Engine",
                        () -> templateEngineDocumentService.generatePdfFromTemplate(templateName, placeholders),
                        templateName, placeholders);

            case "docx-only":
                return tryWithFallback("DOCX Only",
                        () -> docxOnlyService.generateFilledDocx(templateName, placeholders),
                        templateName, placeholders);

            case "auto":
                return tryAllStrategies(templateName, placeholders);

            default:
                throw new IllegalArgumentException("Unknown generation strategy: " + generationStrategy);
        }
    }

    /**
     * Try with fallback to other strategies
     */
    private byte[] tryWithFallback(String strategyName, PdfGenerationTask task, String templateName,
            Map<String, String> placeholders) throws Exception {
        // Try primary strategy
        try {
            log.info("Trying {} strategy", strategyName);
            return task.execute();
        } catch (Exception e) {
            log.warn("{} strategy failed: {}", strategyName, e.getMessage());
        }

        // Fallback to other strategies
        return tryAllStrategies(templateName, placeholders);
    }

    /**
     * Try all available strategies
     */
    private byte[] tryAllStrategies(String templateName, Map<String, String> placeholders) throws Exception {
        Exception lastException = null;

        // Strategy 1: DOCX Only Service (fastest, no LibreOffice)
        try {
            log.info("Trying DOCX Only Service strategy");
            return docxOnlyService.generateFilledDocx(templateName, placeholders);
        } catch (Exception e) {
            log.warn("DOCX Only Service failed: {}", e.getMessage());
            lastException = e;
        }

        // Strategy 2: Improved Document Service
        try {
            log.info("Trying Improved Document Service strategy");
            return improvedDocumentService.generatePdfFromTemplate(templateName, placeholders);
        } catch (Exception e) {
            log.warn("Improved Document Service failed: {}", e.getMessage());
            lastException = e;
        }

        // Strategy 3: Template Engine Service
        try {
            log.info("Trying Template Engine Service strategy");
            return templateEngineDocumentService.generatePdfFromTemplate(templateName, placeholders);
        } catch (Exception e) {
            log.warn("Template Engine Service failed: {}", e.getMessage());
            lastException = e;
        }

        // Strategy 4: Simple Document Service
        try {
            log.info("Trying Simple Document Service strategy");
            return documentTemplateService.generatePdfFromTemplate(templateName, placeholders);
        } catch (Exception e) {
            log.warn("Simple Document Service failed: {}", e.getMessage());
            lastException = e;
        }

        // All strategies failed
        log.error("All document generation strategies failed");
        throw new Exception("All document generation strategies failed. Last error: " +
                (lastException != null ? lastException.getMessage() : "Unknown error"), lastException);
    }

    /**
     * Check if any service is available
     */
    public boolean isAnyServiceAvailable() {
        try {
            // DOCX Only service is always available (no LibreOffice dependency)
            return true;
        } catch (Exception e) {
            log.error("Error checking service availability", e);
            return false;
        }
    }

    /**
     * Get service status
     */
    public String getServiceStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Document Generation Manager Status:\n");

        try {
            status.append("- DOCX Only Service: ").append(docxOnlyService.testService()).append("\n");
            status.append("- Simple Service: ").append(documentTemplateService.testService()).append("\n");
            status.append("- Improved Service: ").append(improvedDocumentService.testService()).append("\n");
            status.append("- Template Engine Service: ").append(templateEngineDocumentService.testService())
                    .append("\n");
            status.append("- Current Strategy: ").append(generationStrategy).append("\n");
            status.append("- Any Service Available: ").append(isAnyServiceAvailable()).append("\n");
        } catch (Exception e) {
            status.append("Error getting status: ").append(e.getMessage());
        }

        return status.toString();
    }

    /**
     * Test all services
     */
    public String testAllServices() {
        StringBuilder result = new StringBuilder();
        result.append("=== Testing All Document Generation Services ===\n");

        try {
            result.append("1. DOCX Only Service: ").append(docxOnlyService.testService()).append("\n");
            result.append("2. Simple Document Service: ").append(documentTemplateService.testService()).append("\n");
            result.append("3. Improved Document Service: ").append(improvedDocumentService.testService()).append("\n");
            result.append("4. Template Engine Service: ").append(templateEngineDocumentService.testService())
                    .append("\n");
            result.append("5. Manager Status: ").append(isAnyServiceAvailable() ? "AVAILABLE" : "NOT AVAILABLE")
                    .append("\n");
        } catch (Exception e) {
            result.append("Error testing services: ").append(e.getMessage());
        }

        return result.toString();
    }

    /**
     * Functional interface for PDF generation tasks
     */
    @FunctionalInterface
    private interface PdfGenerationTask {
        byte[] execute() throws Exception;
    }
}
