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
import com.example.inspection.entity.Dossier;
import com.example.inspection.entity.InspectionType;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.exception.AccessDeniedException;
import com.example.inspection.mapper.DossierMapper;
import com.example.inspection.repository.CustomerRepository;
import com.example.inspection.repository.DossierRepository;
import com.example.inspection.repository.InspectionTypeRepository;
import com.example.inspection.service.DossierService;
import com.example.inspection.service.UserPermissionService;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DossierServiceImpl implements DossierService {

        private final DossierRepository dossierRepository;
        private final CustomerRepository customerRepository;
        private final InspectionTypeRepository inspectionTypeRepository;
        private final DossierMapper dossierMapper;
        private final UserPermissionService userPermissionService;

        private final DataFormatter formatter = new DataFormatter();
        private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        @Override
        public ReceiptResponse createDossier(ReceiptRequest request) {
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

                Dossier dossier = dossierMapper.toEntity(request, submit, related, inspectionType);
                return dossierMapper.toResponse(dossierRepository.save(dossier));
        }

        @Override
        public ReceiptResponse getDossierById(Long id) {
                Dossier dossier = dossierRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Dossier not found"));
                return dossierMapper.toResponse(dossier);
        }

        @Override
        public Page<ReceiptResponse> getAll(int page, int size) {
                Pageable pageable = PageRequest.of(page, size);
                return dossierRepository.findAll(pageable).map(dossierMapper::toResponse);
        }

        @Override
        public List<ReceiptResponse> getAllDossiers() {
                return dossierRepository.findAll()
                                .stream()
                                .map(dossierMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public ReceiptResponse findByRegistrationNo(String registrationNo) {
                Dossier dossier = dossierRepository.findByRegistrationNo(registrationNo)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Dossier not found with registrationNo: " + registrationNo));
                return dossierMapper.toResponse(dossier);
        }

        @Override
        public ReceiptResponse updateDossier(Long id, ReceiptRequest request) {
                Dossier dossier = dossierRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found"));

                // If dossier is OBTAINED, block updates for ISO_STAFF and DOCUMENT_STAFF
                // (read-only)
                if (dossier.getCertificateStatus() == Dossier.CertificateStatus.OBTAINED) {
                        if (userPermissionService.isIsoStaff() || userPermissionService.isDocumentStaff()) {
                                throw new AccessDeniedException("Hồ sơ đã đạt - chỉ được xem, không được sửa");
                        }
                }

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

                Dossier updated = dossierMapper.toEntity(request, submit, related, inspectionType);
                updated.setDossierId(dossier.getDossierId());
                return dossierMapper.toResponse(dossierRepository.save(updated));
        }

        @Override
        public void deleteDossier(Long id) {
                Dossier dossier = dossierRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Dossier not found"));
                dossierRepository.delete(dossier);
        }

        @Override
        @Transactional
        public Dossier createDossierFromSheet(Sheet sheet, Customer customer) {
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

                Dossier dossier = new Dossier();
                dossier.setInspectionType(inspectionType);
                dossier.setCustomerSubmit(customer);
                dossier.setCustomerRelated(customer);

                dossier.setBillOfLading(billOfLading);
                dossier.setBillOfLadingDate(billOfLadingDate);
                dossier.setDeclarationNo(declarationNo);
                dossier.setDeclarationDate(declarationDate);
                dossier.setRegistrationNo(registrationNo);
                dossier.setRegistrationDate(registrationDate);

                return dossierRepository.save(dossier);
        }
}
