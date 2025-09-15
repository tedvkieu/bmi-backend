package com.example.inspection.implementation;

import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.MachineRequest;
import com.example.inspection.dto.response.MachineResponse;
import com.example.inspection.entity.Dossier;
import com.example.inspection.entity.Machine;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.mapper.MachineMapper;
import com.example.inspection.repository.DossierRepository;
import com.example.inspection.repository.MachineRepository;
import com.example.inspection.service.DocumentGenerationService;
import com.example.inspection.service.MachineService;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;
    private final DossierRepository dossierRepository;
    private final MachineMapper machineMapper;
    private final DocumentGenerationService documentGenerationService;

    @Override
    public MachineResponse create(MachineRequest request) {
        System.out.println("Creating Machine with request: " + request);
        Dossier dossier = dossierRepository.findById(request.getDossierId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Dossier not found with id: " + request.getDossierId()));

        System.out.println("Found Dossier: " + dossier);
        Machine machine = machineMapper.toEntity(request, dossier);
        System.out.println("Creating Machine: " + machine);
        Machine machineSaved = machineRepository.save(machine);
        // try {
        // String reportFileName =
        // documentGenerationService.generateInspectionReport(dossier.getDossierId());
        // } catch (Exception e) {
        // throw new RuntimeException("Failed to generate inspection report", e);
        // }
        return machineMapper.toResponse(machineSaved);
    }

    @Override
    public MachineResponse getById(Long id) {
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Machine not found with id: " + id));
        return machineMapper.toResponse(machine);
    }

    @Override
    public List<MachineResponse> getAll() {
        return machineRepository.findAll()
                .stream()
                .map(machineMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MachineResponse update(Long id, MachineRequest request) {
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Machine not found with id: " + id));

        Dossier dossier = dossierRepository.findById(request.getDossierId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Dossier not found with id: " + request.getDossierId()));

        machineMapper.updateEntity(machine, request, dossier);
        return machineMapper.toResponse(machineRepository.save(machine));
    }

    @Override
    public void delete(Long id) {
        if (!machineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Machine not found with id: " + id);
        }
        machineRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void importMachinesFromSheet(Sheet sheet, Long dossierId) {
        Dossier dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new IllegalArgumentException("Dossier not found with id: " + dossierId));

        DataFormatter formatter = new DataFormatter();

        for (int rowIdx = 5; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
            Row row = sheet.getRow(rowIdx);
            if (row == null)
                continue;

            String itemName = formatter.formatCellValue(row.getCell(1)).trim(); // B
            if (itemName.isEmpty())
                break;

            Machine machine = new Machine();
            machine.setDossier(dossier);
            machine.setItemName(itemName);
            machine.setBrand(formatter.formatCellValue(row.getCell(2)).trim()); // C
            machine.setModel(formatter.formatCellValue(row.getCell(3)).trim()); // D
            machine.setSerialNumber(formatter.formatCellValue(row.getCell(4)).trim()); // E
            machine.setManufactureCountry(formatter.formatCellValue(row.getCell(5)).trim()); // F
            machine.setManufacturerName(formatter.formatCellValue(row.getCell(6)).trim()); // G

            String yearStr = formatter.formatCellValue(row.getCell(7)).trim(); // H
            machine.setManufactureYear(yearStr.isEmpty() ? null : Integer.parseInt(yearStr));

            String qtyStr = formatter.formatCellValue(row.getCell(8)).trim(); // I
            machine.setQuantity(qtyStr.isEmpty() ? null : Integer.parseInt(qtyStr));

            machine.setNote(formatter.formatCellValue(row.getCell(9)).trim()); // J

            machineRepository.save(machine);
        }
    }
}
