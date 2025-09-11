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
import com.example.inspection.entity.Machine;
import com.example.inspection.entity.Receipt;
import com.example.inspection.mapper.CustomerMapper;
import com.example.inspection.mapper.MachineMapper;
import com.example.inspection.repository.MachineRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportExcelService {

    private final CustomerService customerImportService;
    private final ReceiptService receiptImportService;
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
            Receipt receipt = receiptImportService.createReceiptFromSheet(sheet1, customer);

            // Sheet 2
            Sheet sheet2 = workbook.getSheetAt(1);
            machineImportService.importMachinesFromSheet(sheet2, receipt.getReceiptId());

            // Lấy lại danh sách machine sau khi save
            List<Machine> machines = machineRepository.findByReceipt_ReceiptId(receipt.getReceiptId());

            // Map sang DTO
            CustomerResponse customerDTO = customerMapper.toResponse(customer);

            List<MachineResponse> machineDTOs = machines.stream()
                    .map(machineMapper::toResponse)
                    .toList();

            return new ReceiptImportDTO(
                    receipt.getReceiptId(),
                    receipt.getDeclarationNo(),
                    receipt.getDeclarationDate(),
                    receipt.getRegistrationNo(),
                    receipt.getRegistrationDate(),
                    receipt.getBillOfLading(),
                    receipt.getBillOfLadingDate(),
                    customerDTO,
                    machineDTOs);
        }
    }

}
