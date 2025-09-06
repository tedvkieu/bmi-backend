package com.example.inspection.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.response.InspectionReportData;
import com.example.inspection.entity.Customer;
import com.example.inspection.entity.Machine;
import com.example.inspection.entity.Receipt;
import com.example.inspection.repository.ReceiptRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InspectionReportService {

    private final ReceiptRepository receiptRepository;
    private final DocumentGenerationManager documentGenerationManager; // Sử dụng manager mới

    /**
     * Generate inspection report PDF with detailed logging and error handling
     */
    public byte[] generateInspectionReportPdf(Long receiptId, String templateName) throws Exception {
        log.info("=== Starting PDF generation for receipt ID: {} with template: {} ===", receiptId, templateName);

        try {
            // Validate inputs
            if (receiptId == null || receiptId <= 0) {
                throw new IllegalArgumentException("Invalid receipt ID: " + receiptId);
            }

            if (templateName == null || templateName.trim().isEmpty()) {
                throw new IllegalArgumentException("Template name cannot be null or empty");
            }

            // Check service availability first
            log.info("Checking document generation service availability...");
            if (!documentGenerationManager.isAnyServiceAvailable()) {
                throw new Exception(
                        "No document generation service is available - cannot generate PDF. Please check LibreOffice installation.");
            }
            log.info("Document generation service is available");

            // Get data from database
            log.info("Step 1: Retrieving data from database for receipt ID: {}", receiptId);
            InspectionReportData reportData = getInspectionReportData(receiptId);
            log.info("Step 1: Successfully retrieved report data");

            // Convert to placeholders map
            log.info("Step 2: Converting data to placeholders");
            Map<String, String> placeholders = convertToPlaceholders(reportData);
            log.info("Step 2: Created {} placeholders", placeholders.size());

            // Log some key placeholders for debugging
            log.debug("Key placeholders: importerName='{}', itemName='{}', registrationNo='{}'",
                    placeholders.get("importerName"),
                    placeholders.get("itemName"),
                    placeholders.get("registrationNo"));

            // Generate PDF
            log.info("Step 3: Starting PDF generation with LibreOffice");
            long startTime = System.currentTimeMillis();

            byte[] pdfBytes = documentGenerationManager.generatePdfFromTemplate(templateName, placeholders);

            long duration = System.currentTimeMillis() - startTime;
            log.info("Step 3: PDF generation completed in {} ms", duration);

            if (pdfBytes == null || pdfBytes.length == 0) {
                throw new Exception("Generated PDF is empty - check template and LibreOffice conversion");
            }

            log.info("=== Successfully generated PDF for receipt ID: {}. Size: {} bytes ===", receiptId,
                    pdfBytes.length);
            return pdfBytes;

        } catch (IllegalArgumentException e) {
            log.error("Invalid argument for receipt ID {}: {}", receiptId, e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            log.error("Runtime error generating PDF for receipt ID {}: {}", receiptId, e.getMessage(), e);
            throw new Exception("Failed to generate inspection report PDF: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("=== Error generating PDF for receipt ID: {} ===", receiptId, e);
            throw new Exception("Failed to generate inspection report PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Get inspection report data from database
     */
    public InspectionReportData getInspectionReportData(Long receiptId) {
        log.debug("Fetching inspection report data for receipt ID: {}", receiptId);

        Optional<Receipt> receiptOpt = receiptRepository.findByIdWithDetails(receiptId);

        if (receiptOpt.isEmpty()) {
            log.error("Receipt not found with ID: {}", receiptId);
            throw new RuntimeException("Receipt not found with ID: " + receiptId);
        }

        Receipt receipt = receiptOpt.get();
        InspectionReportData data = new InspectionReportData();

        log.debug("Processing receipt data. Receipt registration no: {}", receipt.getRegistrationNo());

        // Customer information (customerRelated is the importer)
        Customer importer = receipt.getCustomerRelated();
        if (importer != null) {
            log.debug("Processing importer data: {}", importer.getName());
            data.setImporterName(importer.getName());
            data.setAddress(importer.getAddress());
            data.setPhone(importer.getPhone());
            data.setEmail(importer.getEmail());
        } else {
            log.warn("No importer (customerRelated) found for receipt ID: {}", receiptId);
            data.setImporterName("");
            data.setAddress("");
            data.setPhone("");
            data.setEmail("");
        }

        // Get tax code from InspectionFile if available
        // Note: You might need to add a relationship or query to get InspectionFile
        // For now, using empty string as placeholder
        data.setTaxCode(""); // TODO: Implement logic to get tax code from InspectionFile

        // Receipt information
        data.setBillOfLading(receipt.getBillOfLading());
        data.setDeclarationNo(receipt.getDeclarationNo());
        data.setRegistrationNo(receipt.getRegistrationNo());
        data.setInspectionLocation(receipt.getInspectionLocation());

        // Format dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        data.setCurrentDate(LocalDate.now().format(formatter));

        if (receipt.getInspectionDate() != null) {
            data.setInspectionDate(receipt.getInspectionDate().format(formatter));
        } else {
            data.setInspectionDate("");
        }

        // Machine information (get first machine)
        if (receipt.getMachines() != null && !receipt.getMachines().isEmpty()) {
            Machine firstMachine = receipt.getMachines().get(0);
            log.debug("Processing machine data: {}", firstMachine.getItemName());

            data.setItemName(firstMachine.getItemName());
            data.setBrand(firstMachine.getBrand());
            data.setModel(firstMachine.getModel());
            data.setSerialNumber(firstMachine.getSerialNumber());
            data.setManufacturerName(firstMachine.getManufacturerName());
            data.setManufactureCountry(firstMachine.getManufactureCountry());

            if (firstMachine.getManufactureYear() != null) {
                data.setManufactureYear(firstMachine.getManufactureYear().toString());
            } else {
                data.setManufactureYear("");
            }

            if (firstMachine.getQuantity() != null) {
                data.setQuantity(firstMachine.getQuantity().toString());
            } else {
                data.setQuantity("");
            }
        } else {
            log.warn("No machines found for receipt ID: {}", receiptId);
            // Set empty values for machine fields
            data.setItemName("");
            data.setBrand("");
            data.setModel("");
            data.setSerialNumber("");
            data.setManufacturerName("");
            data.setManufactureCountry("");
            data.setManufactureYear("");
            data.setQuantity("");
        }

        log.debug("Successfully prepared report data for receipt ID: {}", receiptId);
        return data;
    }

    /**
     * Convert report data to placeholders map
     */
    public Map<String, String> convertToPlaceholders(InspectionReportData data) {
        Map<String, String> placeholders = new HashMap<>();

        // Customer information
        placeholders.put("importerName", data.getImporterName());
        placeholders.put("address", data.getAddress());
        placeholders.put("taxCode", data.getTaxCode());
        placeholders.put("phone", data.getPhone());
        placeholders.put("email", data.getEmail());

        // Receipt information
        placeholders.put("billOfLading", data.getBillOfLading());
        placeholders.put("declarationNo", data.getDeclarationNo());
        placeholders.put("registrationNo", data.getRegistrationNo());

        // Machine information
        placeholders.put("itemName", data.getItemName());
        placeholders.put("brand", data.getBrand());
        placeholders.put("model", data.getModel());
        placeholders.put("serialNumber", data.getSerialNumber());
        placeholders.put("manufactureYear", data.getManufactureYear());
        placeholders.put("manufacturerName", data.getManufacturerName());
        placeholders.put("manufactureCountry", data.getManufactureCountry());
        placeholders.put("quantity", data.getQuantity());

        // Date and location information
        placeholders.put("currentDate", data.getCurrentDate());
        placeholders.put("inspectionDate", data.getInspectionDate());
        placeholders.put("inspectionLocation", data.getInspectionLocation());

        // Replace null values with empty strings to avoid "null" text in document
        placeholders.replaceAll((key, value) -> value != null ? value : "");

        log.debug("Created {} placeholders for template replacement", placeholders.size());

        // Log placeholder values for debugging (be careful with sensitive data)
        if (log.isDebugEnabled()) {
            placeholders.forEach((key, value) -> {
                // Only log non-sensitive data
                if (!key.toLowerCase().contains("phone") && !key.toLowerCase().contains("email")) {
                    log.debug("Placeholder: {} = '{}'", key, value);
                }
            });
        }

        return placeholders;
    }

    /**
     * Get inspection report data with additional validation
     */
    public InspectionReportData getInspectionReportDataWithValidation(Long receiptId) {
        if (receiptId == null || receiptId <= 0) {
            throw new IllegalArgumentException("Invalid receipt ID: " + receiptId);
        }

        return getInspectionReportData(receiptId);
    }

    /**
     * Check if receipt exists and has required data for report generation
     */
    public boolean canGenerateReport(Long receiptId) {
        try {
            if (receiptId == null || receiptId <= 0) {
                log.warn("Invalid receipt ID for canGenerateReport: {}", receiptId);
                return false;
            }

            Optional<Receipt> receiptOpt = receiptRepository.findByIdWithDetails(receiptId);
            if (receiptOpt.isEmpty()) {
                log.warn("Receipt not found for canGenerateReport: {}", receiptId);
                return false;
            }

            Receipt receipt = receiptOpt.get();

            // Check if has minimum required data
            boolean hasCustomer = receipt.getCustomerRelated() != null;
            boolean hasMachines = receipt.getMachines() != null && !receipt.getMachines().isEmpty();

            boolean canGenerate = hasCustomer && hasMachines;

            log.debug("Can generate report for receipt {}: customer={}, machines={}, result={}",
                    receiptId, hasCustomer, hasMachines, canGenerate);

            return canGenerate;

        } catch (Exception e) {
            log.error("Error checking if can generate report for receipt ID: {}", receiptId, e);
            return false;
        }
    }

    /**
     * Check if any document generation service is available
     */
    public boolean isLibreOfficeAvailable() {
        try {
            return documentGenerationManager.isAnyServiceAvailable();
        } catch (Exception e) {
            log.error("Error checking document generation service availability", e);
            return false;
        }
    }

    /**
     * Test method to verify service is working
     */
    public String testService() {
        try {
            boolean serviceAvailable = isLibreOfficeAvailable();
            String managerStatus = documentGenerationManager.getServiceStatus();
            return "InspectionReportService is working. Service available: " + serviceAvailable + "\n" + managerStatus;
        } catch (Exception e) {
            return "InspectionReportService error: " + e.getMessage();
        }
    }
}