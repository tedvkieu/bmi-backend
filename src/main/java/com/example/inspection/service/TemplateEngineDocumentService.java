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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Template Engine Document Service
 * Sử dụng cách tiếp cận template engine để xử lý DOCX files
 * Tập trung vào việc preserve formatting và xử lý placeholders chính xác
 */
@Service
@Slf4j
public class TemplateEngineDocumentService {

    private final ResourceLoader resourceLoader;

    @Value("${app.template.path:classpath:templates/}")
    private String templatePath;

    @Value("${app.libreoffice.path:/Applications/LibreOffice.app/Contents/MacOS/soffice}")
    private String libreOfficePath;

    @Value("${app.temp.path:/tmp/}")
    private String tempPath;

    // Pattern để tìm placeholders
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    public TemplateEngineDocumentService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Generate PDF from DOCX template using template engine approach
     */
    public byte[] generatePdfFromTemplate(String templateName, Map<String, String> placeholders)
            throws Exception {

        log.info("=== Starting Template Engine PDF generation ===");
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

        Path tempDocxPath = tempDir.resolve("template_engine_" + timestamp + ".docx");
        Path tempPdfPath = tempDir.resolve("output_" + timestamp + ".pdf");

        try {
            // Step 1: Copy template to temp location
            log.info("Step 1: Copying template to temp location");
            try (InputStream templateStream = templateResource.getInputStream()) {
                Files.copy(templateStream, tempDocxPath, StandardCopyOption.REPLACE_EXISTING);
            }
            log.info("Template copied to: {}", tempDocxPath);

            // Step 2: Process template with template engine
            log.info("Step 2: Processing template with template engine");
            processTemplateWithEngine(tempDocxPath, placeholders);
            log.info("Template processed successfully");

            // Step 3: Convert to PDF
            log.info("Step 3: Converting to PDF");
            convertDocxToPdfWithEngine(tempDocxPath, tempPdfPath);
            log.info("PDF conversion completed");

            // Step 4: Read PDF
            if (!Files.exists(tempPdfPath)) {
                throw new Exception("PDF file was not generated: " + tempPdfPath);
            }

            byte[] pdfBytes = Files.readAllBytes(tempPdfPath);
            log.info("PDF generated successfully. Size: {} bytes", pdfBytes.length);

            return pdfBytes;

        } catch (Exception e) {
            log.error("Error in template engine PDF generation: ", e);
            throw e;
        } finally {
            // Cleanup
            cleanupTempFiles(tempDocxPath, tempPdfPath);
        }
    }

    /**
     * Process template using template engine approach
     * Use Apache POI for proper DOCX processing
     */
    private void processTemplateWithEngine(Path docxPath, Map<String, String> placeholders) throws Exception {
        log.debug("Processing template with engine: {}", docxPath);

        try (XWPFDocument document = new XWPFDocument(Files.newInputStream(docxPath));
                FileOutputStream docxOut = new FileOutputStream(docxPath.toFile())) {

            // Replace in paragraphs
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                replacePlaceholdersInParagraphWithEngine(paragraph, placeholders);
            }

            // Replace in tables
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            replacePlaceholdersInParagraphWithEngine(paragraph, placeholders);
                        }
                    }
                }
            }

            // Replace in headers
            for (XWPFHeader header : document.getHeaderList()) {
                for (XWPFParagraph paragraph : header.getParagraphs()) {
                    replacePlaceholdersInParagraphWithEngine(paragraph, placeholders);
                }
            }

            // Replace in footers
            for (XWPFFooter footer : document.getFooterList()) {
                for (XWPFParagraph paragraph : footer.getParagraphs()) {
                    replacePlaceholdersInParagraphWithEngine(paragraph, placeholders);
                }
            }

            // Save the document
            document.write(docxOut);
            log.debug("Updated DOCX file with template engine processing");
        }
    }

    /**
     * Replace placeholders in paragraph using template engine approach
     */
    private void replacePlaceholdersInParagraphWithEngine(XWPFParagraph paragraph, Map<String, String> placeholders) {
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
        String newText = replacePlaceholdersWithRegex(originalText, placeholders);

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

            log.debug("Updated paragraph with engine: '{}' -> '{}'", originalText, newText);
        }
    }

    /**
     * Replace placeholders using regex pattern matching
     */
    private String replacePlaceholdersWithRegex(String content, Map<String, String> placeholders) {
        log.debug("Replacing placeholders with regex pattern matching");

        StringBuffer result = new StringBuffer();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(content);

        while (matcher.find()) {
            String placeholderKey = matcher.group(1);
            String replacement = placeholders.getOrDefault(placeholderKey, "");

            // Escape special regex characters in replacement
            String escapedReplacement = Matcher.quoteReplacement(replacement);
            matcher.appendReplacement(result, escapedReplacement);

            log.debug("Replaced placeholder: ${} -> '{}'", placeholderKey, replacement);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * Alternative approach: Process DOCX as ZIP and replace in document.xml
     * This method is reserved for future implementation
     */
    @SuppressWarnings("unused")
    private void processDocxAsZip(Path docxPath, Map<String, String> placeholders) throws Exception {
        log.debug("Processing DOCX as ZIP file: {}", docxPath);

        // This is a more complex approach that would involve:
        // 1. Extract DOCX (ZIP) to temp directory
        // 2. Find and process word/document.xml
        // 3. Replace placeholders in XML
        // 4. Re-zip the DOCX file

        // For now, we'll use the simpler string replacement approach
        // This can be implemented later if needed
        throw new UnsupportedOperationException("ZIP processing not implemented yet");
    }

    /**
     * Convert DOCX to PDF with engine-specific optimizations
     */
    private void convertDocxToPdfWithEngine(Path inputPath, Path outputPath) throws Exception {
        log.info("Converting DOCX to PDF with engine: {} -> {}", inputPath, outputPath);

        // Create output directory
        Path outputDir = outputPath.getParent();
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        // LibreOffice command with engine-specific options
        String command = String.format(
                "%s --headless --convert-to pdf --outdir %s %s",
                libreOfficePath,
                outputDir.toAbsolutePath(),
                inputPath.toAbsolutePath());

        log.debug("Executing engine command: {}", command);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", "-c", command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        // Capture output
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                log.debug("LibreOffice output: {}", line);
            }
        }

        // Wait for completion
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

        log.info("Engine LibreOffice conversion completed successfully");
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
     * Test method
     */
    public String testService() {
        try {
            boolean libreOfficeAvailable = isLibreOfficeAvailable();
            return "TemplateEngineDocumentService is working. LibreOffice available: " + libreOfficeAvailable;
        } catch (Exception e) {
            return "TemplateEngineDocumentService error: " + e.getMessage();
        }
    }
}
