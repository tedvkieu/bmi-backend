package com.example.inspection.service;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.inspection.dto.response.CustomerResponse;
import com.example.inspection.dto.response.MachineResponse;
import com.example.inspection.dto.response.ReceiptImportDTO;
import com.example.inspection.entity.Customer;
import com.example.inspection.entity.Dossier;
import com.example.inspection.entity.Machine;
import com.example.inspection.mapper.CustomerMapper;
import com.example.inspection.mapper.MachineMapper;
import com.example.inspection.repository.MachineRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportExcelService {

    private final CustomerService customerImportService;
    private final DossierService dossierService;
    private final MachineService machineImportService;
    private final MachineRepository machineRepository;
    private final CustomerMapper customerMapper;
    private final MachineMapper machineMapper;

    @Transactional
    public ReceiptImportDTO importFromExcel(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);

            // Sheet 1
            Sheet sheet1 = workbook.getSheetAt(0);
            Customer customer = customerImportService.createCustomerFromSheet(sheet1);
            Dossier dossier = dossierService.createDossierFromSheet(sheet1, customer);

            // Sheet 2
            Sheet sheet2 = workbook.getSheetAt(1);
            machineImportService.importMachinesFromSheet(sheet2, dossier.getDossierId());

            // Lấy lại danh sách machine sau khi save
            List<Machine> machines = machineRepository.findByDossier_DossierId(dossier.getDossierId());

            // Map sang DTO
            CustomerResponse customerDTO = customerMapper.toResponse(customer);

            List<MachineResponse> machineDTOs = machines.stream()
                    .map(machineMapper::toResponse)
                    .toList();

            return new ReceiptImportDTO(
                    dossier.getDossierId(),
                    dossier.getDeclarationNo(),
                    dossier.getDeclarationDate(),
                    dossier.getRegistrationNo(),
                    dossier.getRegistrationDate(),
                    dossier.getBillOfLading(),
                    dossier.getBillOfLadingDate(),
                    customerDTO,
                    machineDTOs);
        }
    }

}
