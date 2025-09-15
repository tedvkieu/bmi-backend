package com.example.inspection.service;

import com.example.inspection.entity.Machine;
import com.example.inspection.mapper.DossierMapper;
import com.example.inspection.repository.DossierRepository;
import com.example.inspection.dto.response.ReceiptResponse;
import com.example.inspection.entity.Customer;
import com.example.inspection.entity.Dossier;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DocumentGenerationService {

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private DossierMapper dossierMapper;

    private static final String TEMPLATE_PATH = "templates/inspection_report_template.docx";
    private static final String EXPORT_DIRECTORY = "uploads/exports/";

    public ReceiptResponse generateInspectionReport(Long receiptId) throws Exception {
        // Get receipt data with all related entities
        Dossier dossier = dossierRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found with id: " +
                        receiptId));

        // Load template
        ClassPathResource templateResource = new ClassPathResource(TEMPLATE_PATH);
        XWPFDocument document = new XWPFDocument(templateResource.getInputStream());

        // Get data for replacement
        Customer importer = dossier.getCustomerRelated();

        // InspectionFile inspectionFile =
        // dossier.getCustomerSubmit().getInspectionFiles() != null
        // && !dossier.getCustomerSubmit().getInspectionFiles().isEmpty()
        // ? dossier.getCustomerSubmit().getInspectionFiles().get(0)
        // : null;
        List<Machine> machines = dossier.getMachines();
        Machine firstMachine = machines.isEmpty() ? null : machines.get(0);

        // Replace placeholders in document
        replaceTextInDocument(document, "${importerName}",
                importer != null ? importer.getName() : "");
        replaceTextInDocument(document, "${address}",
                importer != null ? importer.getAddress() : "");
        replaceTextInDocument(document, "${taxCode}", importer != null ? importer.getTaxCode() : "");
        replaceTextInDocument(document, "${phone}",
                importer != null ? importer.getPhone() : "");
        replaceTextInDocument(document, "${email}",
                importer != null ? importer.getEmail() : "");
        replaceTextInDocument(document, "${billOfLading}",
                dossier.getBillOfLading() != null ? dossier.getBillOfLading() : "");
        replaceTextInDocument(document, "${declarationNo}",
                dossier.getDeclarationNo() != null ? dossier.getDeclarationNo() : "");
        replaceTextInDocument(document, "${registrationNo}",
                dossier.getRegistrationNo() != null ? dossier.getRegistrationNo() : "");

        // Machine information (first machine)
        if (firstMachine != null) {
            replaceTextInDocument(document, "${itemName}",
                    firstMachine.getItemName() != null ? firstMachine.getItemName() : "");
            replaceTextInDocument(document, "${brand}",
                    firstMachine.getBrand() != null ? firstMachine.getBrand() : "");
            replaceTextInDocument(document, "${model}",
                    firstMachine.getModel() != null ? firstMachine.getModel() : "");
            replaceTextInDocument(document, "${serialNumber}",
                    firstMachine.getSerialNumber() != null ? firstMachine.getSerialNumber() : "");
            replaceTextInDocument(document, "${manufactureYear}",
                    firstMachine.getManufactureYear() != null ? firstMachine.getManufactureYear().toString() : "");
            replaceTextInDocument(document, "${manufacturerName}",
                    firstMachine.getManufacturerName() != null ? firstMachine.getManufacturerName() : "");
            replaceTextInDocument(document, "${manufactureCountry}",
                    firstMachine.getManufactureCountry() != null ? firstMachine.getManufactureCountry() : "");
            replaceTextInDocument(document, "${quantity}",
                    firstMachine.getQuantity() != null ? firstMachine.getQuantity().toString() : "");
        } else {
            // Clear machine placeholders if no machine data
            clearMachinePlaceholders(document);
        }

        // Create exports directory if it doesn't exist
        Path exportDir = Paths.get(EXPORT_DIRECTORY);
        if (!Files.exists(exportDir)) {
            Files.createDirectories(exportDir);
        }

        // ✅ Đặt tên file: có billOfLading
        String billOfLading = dossier.getBillOfLading() != null ? dossier.getBillOfLading() : "no-bol";
        billOfLading = billOfLading.replaceAll("[^a-zA-Z0-9_-]", "_"); // tránh ký tự lạ

        String fileName = "inspection_report_" + billOfLading + "_" + receiptId + "_" +
                System.currentTimeMillis() + ".docx";
        String filePath = EXPORT_DIRECTORY + fileName;

        try (FileOutputStream out = new FileOutputStream(filePath)) {
            document.write(out);
        }
        document.close();

        // ✅ Lưu đường dẫn vào entity
        dossier.setFiles(fileName);
        System.out.println("Generated document path: " + fileName);
        dossierRepository.save(dossier);

        return dossierMapper.toResponse(dossier);
    }

    private void replaceTextInDocument(XWPFDocument document, String placeholder,
            String replacement) {

        if (replacement == null) {
            replacement = "";
        }
        // Replace in paragraphs
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceTextInParagraph(paragraph, placeholder, replacement);
        }

        // Replace in tables
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceTextInParagraph(paragraph, placeholder, replacement);
                    }
                }
            }
        }

        // Replace in headers
        for (XWPFHeader header : document.getHeaderList()) {
            for (XWPFParagraph paragraph : header.getParagraphs()) {
                replaceTextInParagraph(paragraph, placeholder, replacement);
            }
            for (XWPFTable table : header.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            replaceTextInParagraph(paragraph, placeholder, replacement);
                        }
                    }
                }
            }
        }

        // Replace in footers
        for (XWPFFooter footer : document.getFooterList()) {
            for (XWPFParagraph paragraph : footer.getParagraphs()) {
                replaceTextInParagraph(paragraph, placeholder, replacement);
            }
            for (XWPFTable table : footer.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            replaceTextInParagraph(paragraph, placeholder, replacement);
                        }
                    }
                }
            }
        }
    }

    private void replaceTextInParagraph(XWPFParagraph paragraph, String placeholder, String replacement) {
        String text = paragraph.getText();
        if (text != null && text.contains(placeholder)) {
            // Get all runs and rebuild paragraph text
            List<XWPFRun> runs = paragraph.getRuns();

            // Clear all runs
            for (int i = runs.size() - 1; i >= 0; i--) {
                paragraph.removeRun(i);
            }

            // Replace text and add new run
            String newText = text.replace(placeholder, replacement);
            XWPFRun newRun = paragraph.createRun();
            newRun.setText(newText);
        }
    }

    private void clearMachinePlaceholders(XWPFDocument document) {
        replaceTextInDocument(document, "${itemName}", "");
        replaceTextInDocument(document, "${brand}", "");
        replaceTextInDocument(document, "${model}", "");
        replaceTextInDocument(document, "${serialNumber}", "");
        replaceTextInDocument(document, "${manufactureYear}", "");
        replaceTextInDocument(document, "${manufacturerName}", "");
        replaceTextInDocument(document, "${manufactureCountry}", "");
        replaceTextInDocument(document, "${quantity}", "");
    }
}