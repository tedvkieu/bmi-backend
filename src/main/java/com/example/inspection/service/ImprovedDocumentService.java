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
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Improved Document Service với cách tiếp cận Apache POI được cải thiện
 * Tập trung vào việc preserve formatting và xử lý placeholders tốt hơn
 */
@Service
@Slf4j
public class ImprovedDocumentService {

    private final ResourceLoader resourceLoader;

    @Value("${app.template.path:classpath:templates/}")
    private String templatePath;

    @Value("${app.libreoffice.path:/Applications/LibreOffice.app/Contents/MacOS/soffice}")
    private String libreOfficePath;

    @Value("${app.temp.path:/tmp/}")
    private String tempPath;

    public ImprovedDocumentService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Generate PDF from DOCX template với cách tiếp cận cải thiện
     */
    public byte[] generatePdfFromTemplate(String templateName, Map<String, String> placeholders)
            throws Exception {

        log.info("=== Starting improved PDF generation ===");
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

        Path tempDocxPath = tempDir.resolve("improved_" + timestamp + ".docx");
        Path tempPdfPath = tempDir.resolve("output_" + timestamp + ".pdf");

        try (InputStream templateStream = templateResource.getInputStream()) {

            // Step 1: Load and process document with Apache POI
            log.info("Step 1: Loading and processing document with Apache POI");
            XWPFDocument document = new XWPFDocument(templateStream);

            // Step 2: Replace placeholders with improved method
            log.info("Step 2: Replacing placeholders with improved method");
            replacePlaceholdersImproved(document, placeholders);

            // Step 3: Save processed document
            log.info("Step 3: Saving processed document");
            try (FileOutputStream docxOut = new FileOutputStream(tempDocxPath.toFile())) {
                document.write(docxOut);
                docxOut.flush();
            }
            document.close();

            log.info("Document saved to: {}", tempDocxPath);
            log.info("File size: {} bytes", Files.size(tempDocxPath));

            // Step 4: Convert to PDF
            log.info("Step 4: Converting to PDF");
            convertDocxToPdfImproved(tempDocxPath, tempPdfPath);

            // Step 5: Read PDF
            if (!Files.exists(tempPdfPath)) {
                throw new Exception("PDF file was not generated: " + tempPdfPath);
            }

            byte[] pdfBytes = Files.readAllBytes(tempPdfPath);
            log.info("PDF generated successfully. Size: {} bytes", pdfBytes.length);

            return pdfBytes;

        } catch (Exception e) {
            log.error("Error in improved PDF generation: ", e);
            throw e;
        } finally {
            // Cleanup
            cleanupTempFiles(tempDocxPath, tempPdfPath);
        }
    }

    /**
     * Improved placeholder replacement method
     * Tập trung vào việc preserve formatting tốt hơn
     */
    private void replacePlaceholdersImproved(XWPFDocument document, Map<String, String> placeholders) {
        log.debug("Starting improved placeholder replacement");

        // Replace in paragraphs
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replacePlaceholdersInParagraphImproved(paragraph, placeholders);
        }

        // Replace in tables
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replacePlaceholdersInParagraphImproved(paragraph, placeholders);
                    }
                }
            }
        }

        // Replace in headers
        for (XWPFHeader header : document.getHeaderList()) {
            for (XWPFParagraph paragraph : header.getParagraphs()) {
                replacePlaceholdersInParagraphImproved(paragraph, placeholders);
            }
        }

        // Replace in footers
        for (XWPFFooter footer : document.getFooterList()) {
            for (XWPFParagraph paragraph : footer.getParagraphs()) {
                replacePlaceholdersInParagraphImproved(paragraph, placeholders);
            }
        }

        log.debug("Completed improved placeholder replacement");
    }

    /**
     * Improved paragraph replacement - preserve formatting better
     */
    private void replacePlaceholdersInParagraphImproved(XWPFParagraph paragraph, Map<String, String> placeholders) {
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
                    // newRun.setFontSize(firstRun.getFontSize()); // Deprecated method
                    newRun.setColor(firstRun.getColor());
                } catch (Exception e) {
                    log.debug("Could not preserve all formatting: {}", e.getMessage());
                }
            }

            log.debug("Updated paragraph: '{}' -> '{}'", originalText, newText);
        }
    }

    /**
     * Improved DOCX to PDF conversion
     */
    private void convertDocxToPdfImproved(Path inputPath, Path outputPath) throws Exception {
        log.info("Converting DOCX to PDF (improved): {} -> {}", inputPath, outputPath);

        // Create output directory
        Path outputDir = outputPath.getParent();
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        // LibreOffice command with better options
        String command = String.format(
                "%s --headless --convert-to pdf:writer_pdf_Export --outdir %s %s",
                libreOfficePath,
                outputDir.toAbsolutePath(),
                inputPath.toAbsolutePath());

        log.debug("Executing improved command: {}", command);

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
        boolean finished = process.waitFor(3, TimeUnit.MINUTES);
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

        log.info("Improved LibreOffice conversion completed successfully");
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
            return "ImprovedDocumentService is working. LibreOffice available: " + libreOfficeAvailable;
        } catch (Exception e) {
            return "ImprovedDocumentService error: " + e.getMessage();
        }
    }
}
