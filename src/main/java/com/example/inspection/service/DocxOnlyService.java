package com.example.inspection.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

/**
 * DOCX Only Service - Chỉ fill placeholder vào DOCX, không chuyển sang PDF
 * Service này nhanh hơn nhiều vì không cần LibreOffice
 */
@Service
@Slf4j
public class DocxOnlyService {

    private final ResourceLoader resourceLoader;

    @Value("${app.template.path:classpath:templates/}")
    private String templatePath;

    @Value("${app.temp.path:/tmp/}")
    private String tempPath;

    public DocxOnlyService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Generate filled DOCX from template - Chỉ fill placeholder, không chuyển PDF
     */
    public byte[] generateFilledDocx(String templateName, Map<String, String> placeholders)
            throws Exception {

        log.info("=== Starting DOCX generation (no PDF conversion) ===");
        log.info("Template: {}", templateName);

        // Load template
        Resource templateResource = resourceLoader.getResource(templatePath + templateName);
        if (!templateResource.exists()) {
            throw new FileNotFoundException("Template not found: " + templatePath + templateName);
        }

        // Create temp directory
        Path tempDir = Files.createDirectories(Path.of(tempPath));
        String timestamp = String.valueOf(System.currentTimeMillis());
        Path tempDocxPath = tempDir.resolve("filled_" + timestamp + ".docx");

        try {
            // Step 1: Copy template to temp location
            log.info("Step 1: Copying template to temp location");
            try (InputStream templateStream = templateResource.getInputStream()) {
                Files.copy(templateStream, tempDocxPath, StandardCopyOption.REPLACE_EXISTING);
            }
            log.info("Template copied to: {}", tempDocxPath);

            // Step 2: Fill placeholders in DOCX file
            log.info("Step 2: Filling placeholders in DOCX file");
            fillPlaceholdersInDocx(tempDocxPath, placeholders);
            log.info("Placeholders filled successfully");

            // Step 3: Read filled DOCX bytes
            byte[] docxBytes = Files.readAllBytes(tempDocxPath);
            log.info("DOCX generated successfully. Size: {} bytes", docxBytes.length);

            return docxBytes;

        } catch (Exception e) {
            log.error("Error in DOCX generation: ", e);
            throw e;
        } finally {
            // Cleanup
            cleanupTempFiles(tempDocxPath);
        }
    }

    /**
     * Fill placeholders in DOCX file using Apache POI
     * Preserves all formatting and structure
     */
    private void fillPlaceholdersInDocx(Path docxPath, Map<String, String> placeholders)
            throws Exception {

        log.debug("Filling placeholders in DOCX file: {}", docxPath);

        try (XWPFDocument document = new XWPFDocument(Files.newInputStream(docxPath));
                FileOutputStream docxOut = new FileOutputStream(docxPath.toFile())) {

            // Fill in paragraphs
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                fillPlaceholdersInParagraph(paragraph, placeholders);
            }

            // Fill in tables
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            fillPlaceholdersInParagraph(paragraph, placeholders);
                        }
                    }
                }
            }

            // Fill in headers
            for (XWPFHeader header : document.getHeaderList()) {
                for (XWPFParagraph paragraph : header.getParagraphs()) {
                    fillPlaceholdersInParagraph(paragraph, placeholders);
                }
            }

            // Fill in footers
            for (XWPFFooter footer : document.getFooterList()) {
                for (XWPFParagraph paragraph : footer.getParagraphs()) {
                    fillPlaceholdersInParagraph(paragraph, placeholders);
                }
            }

            // Save the document
            document.write(docxOut);
            log.debug("Updated DOCX file with filled placeholders");
        }
    }

    /**
     * Fill placeholders in paragraph with enhanced formatting preservation
     */
    private void fillPlaceholdersInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
        String paragraphText = paragraph.getText();
        if (paragraphText == null || paragraphText.trim().isEmpty()) {
            return;
        }

        // Check if paragraph contains any placeholders
        boolean hasPlaceholders = false;
        for (String key : placeholders.keySet()) {
            if (paragraphText.contains("${" + key + "}")) {
                hasPlaceholders = true;
                break;
            }
        }

        if (!hasPlaceholders) {
            return;
        }

        // Get all runs in the paragraph
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs.isEmpty()) {
            return;
        }

        // Build complete text from all runs
        StringBuilder fullText = new StringBuilder();
        for (XWPFRun run : runs) {
            String runText = run.getText(0);
            if (runText != null) {
                fullText.append(runText);
            }
        }

        String originalText = fullText.toString();
        String newText = originalText;

        // Replace placeholders
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String replacement = entry.getValue() != null ? entry.getValue() : "";
            newText = newText.replace(placeholder, replacement);
        }

        // If text changed, update the paragraph
        if (!newText.equals(originalText)) {
            // Clear all runs
            for (int i = runs.size() - 1; i >= 0; i--) {
                paragraph.removeRun(i);
            }

            // Create new run with the updated text
            XWPFRun newRun = paragraph.createRun();
            newRun.setText(newText);

            // Enhanced formatting preservation
            if (!runs.isEmpty()) {
                XWPFRun firstRun = runs.get(0);
                try {
                    // Preserve basic formatting
                    newRun.setBold(firstRun.isBold());
                    newRun.setItalic(firstRun.isItalic());
                    newRun.setFontFamily(firstRun.getFontFamily());
                    newRun.setColor(firstRun.getColor());

                    // Preserve additional formatting if available
                    if (firstRun.getFontSize() != -1) {
                        newRun.setFontSize(firstRun.getFontSize());
                    }

                    // Preserve underline
                    if (firstRun.getUnderline() != UnderlinePatterns.NONE) {
                        newRun.setUnderline(firstRun.getUnderline());
                    }

                    // Preserve strikethrough
                    if (firstRun.isStrikeThrough()) {
                        newRun.setStrikeThrough(true);
                    }

                } catch (Exception e) {
                    log.debug("Could not preserve all formatting: {}", e.getMessage());
                }
            }

            log.debug("Updated paragraph: '{}' -> '{}'", originalText, newText);
        }
    }

    /**
     * Cleanup temporary files
     */
    private void cleanupTempFiles(Path... paths) {
        for (Path path : paths) {
            try {
                if (Files.exists(path)) {
                    Files.delete(path);
                    log.debug("Deleted temp file: {}", path);
                }
            } catch (IOException e) {
                log.warn("Failed to delete temp file: {}", path, e);
            }
        }
    }

    /**
     * Test method to verify service is working
     */
    public String testService() {
        try {
            return "DocxOnlyService is working. No LibreOffice dependency.";
        } catch (Exception e) {
            return "DocxOnlyService error: " + e.getMessage();
        }
    }
}
