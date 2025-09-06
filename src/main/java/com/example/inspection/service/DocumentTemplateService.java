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
import java.util.concurrent.TimeUnit;

/**
 * Service mới để xử lý template DOCX và chuyển đổi sang PDF
 * Sử dụng cách tiếp cận đơn giản hơn: copy template -> replace placeholders ->
 * convert to PDF
 */
@Service
@Slf4j
public class DocumentTemplateService {

    private final ResourceLoader resourceLoader;

    @Value("${app.template.path:classpath:templates/}")
    private String templatePath;

    @Value("${app.libreoffice.path:/Applications/LibreOffice.app/Contents/MacOS/soffice}")
    private String libreOfficePath;

    @Value("${app.temp.path:/tmp/}")
    private String tempPath;

    public DocumentTemplateService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Generate PDF from DOCX template - Approach mới
     * 1. Copy template to temp directory
     * 2. Replace placeholders using simple string replacement
     * 3. Convert to PDF using LibreOffice
     */
    public byte[] generatePdfFromTemplate(String templateName, Map<String, String> placeholders)
            throws Exception {

        log.info("=== Starting PDF generation with new approach ===");
        log.info("Template: {}, LibreOffice path: {}", templateName, libreOfficePath);

        // Validate LibreOffice
        if (!isLibreOfficeAvailable()) {
            throw new Exception("LibreOffice is not available at: " + libreOfficePath);
        }

        // Load template
        Resource templateResource = resourceLoader.getResource(templatePath + templateName);
        if (!templateResource.exists()) {
            throw new FileNotFoundException("Template not found: " + templatePath + templateName);
        }

        // Create temp directory
        Path tempDir = Files.createDirectories(Path.of(tempPath));
        String timestamp = String.valueOf(System.currentTimeMillis());

        Path tempDocxPath = tempDir.resolve("template_" + timestamp + ".docx");
        Path tempPdfPath = tempDir.resolve("output_" + timestamp + ".pdf");

        try {
            // Step 1: Copy template to temp location
            log.info("Step 1: Copying template to temp location");
            try (InputStream templateStream = templateResource.getInputStream()) {
                Files.copy(templateStream, tempDocxPath, StandardCopyOption.REPLACE_EXISTING);
            }
            log.info("Template copied to: {}", tempDocxPath);

            // Step 2: Replace placeholders in DOCX file
            log.info("Step 2: Replacing placeholders in DOCX file");
            replacePlaceholdersInDocxFile(tempDocxPath, placeholders);
            log.info("Placeholders replaced successfully");

            // Step 3: Convert DOCX to PDF using LibreOffice
            log.info("Step 3: Converting DOCX to PDF using LibreOffice");
            convertDocxToPdf(tempDocxPath, tempPdfPath);
            log.info("PDF conversion completed");

            // Step 4: Read PDF bytes
            if (!Files.exists(tempPdfPath)) {
                throw new Exception("PDF file was not generated: " + tempPdfPath);
            }

            byte[] pdfBytes = Files.readAllBytes(tempPdfPath);
            log.info("PDF generated successfully. Size: {} bytes", pdfBytes.length);

            return pdfBytes;

        } catch (Exception e) {
            log.error("Error in PDF generation: ", e);
            throw e;
        } finally {
            // Cleanup
            cleanupTempFiles(tempDocxPath, tempPdfPath);
        }
    }

    /**
     * Replace placeholders in DOCX file using Apache POI
     * This approach preserves formatting and handles DOCX structure correctly
     */
    private void replacePlaceholdersInDocxFile(Path docxPath, Map<String, String> placeholders)
            throws Exception {

        log.debug("Replacing placeholders in DOCX file: {}", docxPath);

        try (XWPFDocument document = new XWPFDocument(Files.newInputStream(docxPath));
                FileOutputStream docxOut = new FileOutputStream(docxPath.toFile())) {

            // Replace in paragraphs
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                replacePlaceholdersInParagraph(paragraph, placeholders);
            }

            // Replace in tables
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            replacePlaceholdersInParagraph(paragraph, placeholders);
                        }
                    }
                }
            }

            // Replace in headers
            for (XWPFHeader header : document.getHeaderList()) {
                for (XWPFParagraph paragraph : header.getParagraphs()) {
                    replacePlaceholdersInParagraph(paragraph, placeholders);
                }
            }

            // Replace in footers
            for (XWPFFooter footer : document.getFooterList()) {
                for (XWPFParagraph paragraph : footer.getParagraphs()) {
                    replacePlaceholdersInParagraph(paragraph, placeholders);
                }
            }

            // Save the document
            document.write(docxOut);
            log.debug("Updated DOCX file with replaced placeholders");
        }
    }

    /**
     * Replace placeholders in paragraph
     */
    private void replacePlaceholdersInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
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

            // Try to preserve formatting from the first original run
            if (!runs.isEmpty()) {
                XWPFRun firstRun = runs.get(0);
                try {
                    newRun.setBold(firstRun.isBold());
                    newRun.setItalic(firstRun.isItalic());
                    newRun.setFontFamily(firstRun.getFontFamily());
                    newRun.setColor(firstRun.getColor());
                } catch (Exception e) {
                    log.debug("Could not preserve all formatting: {}", e.getMessage());
                }
            }

            log.debug("Updated paragraph: '{}' -> '{}'", originalText, newText);
        }
    }

    /**
     * Convert DOCX to PDF using LibreOffice command line
     */
    private void convertDocxToPdf(Path inputPath, Path outputPath) throws Exception {
        log.info("Converting DOCX to PDF: {} -> {}", inputPath, outputPath);

        // Create output directory if it doesn't exist
        Path outputDir = outputPath.getParent();
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        // LibreOffice command
        String command = String.format(
                "%s --headless --convert-to pdf --outdir %s %s",
                libreOfficePath,
                outputDir.toAbsolutePath(),
                inputPath.toAbsolutePath());

        log.debug("Executing command: {}", command);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", "-c", command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        // Capture output for debugging
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                log.debug("LibreOffice output: {}", line);
            }
        }

        // Wait for process to complete
        boolean finished = process.waitFor(2, TimeUnit.MINUTES);
        if (!finished) {
            process.destroyForcibly();
            throw new Exception("LibreOffice conversion timed out");
        }

        int exitCode = process.exitValue();
        if (exitCode != 0) {
            log.error("LibreOffice conversion failed with exit code: {}", exitCode);
            log.error("Output: {}", output.toString());
            throw new Exception("LibreOffice conversion failed with exit code: " + exitCode);
        }

        log.info("LibreOffice conversion completed successfully");
    }

    /**
     * Check if LibreOffice is available
     */
    public boolean isLibreOfficeAvailable() {
        try {
            log.info("Checking LibreOffice availability at: {}", libreOfficePath);

            ProcessBuilder processBuilder = new ProcessBuilder(libreOfficePath, "--version");
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            // Capture output
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(10, TimeUnit.SECONDS);
            boolean available = finished && process.exitValue() == 0;

            log.info("LibreOffice available: {}", available);
            if (available) {
                log.info("LibreOffice version: {}", output.toString().trim());
            } else {
                log.warn("LibreOffice check failed. Output: {}", output.toString().trim());
            }

            return available;
        } catch (Exception e) {
            log.warn("LibreOffice not available: {}", e.getMessage());
            return false;
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
            boolean libreOfficeAvailable = isLibreOfficeAvailable();
            return "DocumentTemplateService is working. LibreOffice available: " + libreOfficeAvailable;
        } catch (Exception e) {
            return "DocumentTemplateService error: " + e.getMessage();
        }
    }
}
