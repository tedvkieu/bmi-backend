package com.example.inspection.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.ReceiptRequest;
import com.example.inspection.dto.response.ReceiptResponse;
import com.example.inspection.entity.Customer;
import com.example.inspection.entity.InspectionType;
import com.example.inspection.entity.Receipt;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.mapper.ReceiptMapper;
import com.example.inspection.repository.CustomerRepository;
import com.example.inspection.repository.InspectionTypeRepository;
import com.example.inspection.repository.ReceiptRepository;
import com.example.inspection.service.ReceiptService;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

        private final ReceiptRepository receiptRepository;
        private final CustomerRepository customerRepository;
        private final InspectionTypeRepository inspectionTypeRepository;
        private final ReceiptMapper receiptMapper;

        private final DataFormatter formatter = new DataFormatter();
        private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        @Override
        public ReceiptResponse createReceipt(ReceiptRequest request) {
                Customer submit = customerRepository.findById(request.getCustomerSubmitId())
                                .orElseThrow(() -> new ResourceNotFoundException("Customer submit not found"));
                Customer related = request.getCustomerRelatedId() != null
                                ? customerRepository.findById(request.getCustomerRelatedId())
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Customer related not found"))
                                : null;
                InspectionType inspectionType = inspectionTypeRepository
                                .findByInspectionTypeId(request.getInspectionTypeId())
                                .orElseThrow(() -> new ResourceNotFoundException("InspectionType not found"));

                Receipt receipt = receiptMapper.toEntity(request, submit, related, inspectionType);
                return receiptMapper.toResponse(receiptRepository.save(receipt));
        }

        @Override
        public ReceiptResponse getReceiptById(Long id) {
                Receipt receipt = receiptRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found"));
                return receiptMapper.toResponse(receipt);
        }

        @Override
        public Page<ReceiptResponse> getAll(int page, int size) {
                Pageable pageable = PageRequest.of(page, size);
                return receiptRepository.findAll(pageable).map(receiptMapper::toResponse);
        }

        @Override
        public List<ReceiptResponse> getAllReceipts() {
                return receiptRepository.findAll()
                                .stream()
                                .map(receiptMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public ReceiptResponse updateReceipt(Long id, ReceiptRequest request) {
                Receipt receipt = receiptRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found"));

                Customer submit = customerRepository.findById(request.getCustomerSubmitId())
                                .orElseThrow(() -> new ResourceNotFoundException("Customer submit not found"));
                Customer related = request.getCustomerRelatedId() != null
                                ? customerRepository.findById(request.getCustomerRelatedId())
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Customer related not found"))
                                : null;
                InspectionType inspectionType = inspectionTypeRepository
                                .findByInspectionTypeId(request.getInspectionTypeId())
                                .orElseThrow(() -> new ResourceNotFoundException("InspectionType not found"));

                Receipt updated = receiptMapper.toEntity(request, submit, related, inspectionType);
                updated.setReceiptId(receipt.getReceiptId());
                return receiptMapper.toResponse(receiptRepository.save(updated));
        }

        @Override
        public void deleteReceipt(Long id) {
                Receipt receipt = receiptRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found"));
                receiptRepository.delete(receipt);
        }

        @Override
        @Transactional
        public Receipt createReceiptFromSheet(Sheet sheet, Customer customer) {
                String inspectionTypeCode = sheet.getRow(11).getCell(5).getStringCellValue(); // F12
                InspectionType inspectionType = inspectionTypeRepository.findByInspectionTypeId("01")
                                .orElseThrow(() -> new RuntimeException(
                                                "InspectionType id=" + inspectionTypeCode + " not found"));

                String billOfLading = sheet.getRow(14).getCell(6).getStringCellValue(); // G15
                String billStr = formatter.formatCellValue(sheet.getRow(14).getCell(12)).trim(); // M15
                LocalDate billOfLadingDate = billStr.isEmpty() ? null : LocalDate.parse(billStr, dtf);

                String declarationNo = sheet.getRow(15).getCell(6).getStringCellValue(); // G16
                String declDateStr = formatter.formatCellValue(sheet.getRow(15).getCell(12)).trim(); // M16
                LocalDate declarationDate = declDateStr.isEmpty() ? null : LocalDate.parse(declDateStr, dtf);

                String registrationNo = sheet.getRow(16).getCell(6).getStringCellValue(); // G17
                String regDateStr = formatter.formatCellValue(sheet.getRow(16).getCell(12)).trim(); // M17
                LocalDate registrationDate = regDateStr.isEmpty() ? null : LocalDate.parse(regDateStr, dtf);

                Receipt receipt = new Receipt();
                receipt.setInspectionType(inspectionType);
                receipt.setCustomerSubmit(customer);
                receipt.setCustomerRelated(customer);

                receipt.setBillOfLading(billOfLading);
                receipt.setBillOfLadingDate(billOfLadingDate);
                receipt.setDeclarationNo(declarationNo);
                receipt.setDeclarationDate(declarationDate);
                receipt.setRegistrationNo(registrationNo);
                receipt.setRegistrationDate(registrationDate);

                return receiptRepository.save(receipt);
        }
}
