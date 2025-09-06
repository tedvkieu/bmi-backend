package com.example.inspection.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.MachineRequest;
import com.example.inspection.dto.response.MachineResponse;
import com.example.inspection.entity.Machine;
import com.example.inspection.entity.Receipt;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.mapper.MachineMapper;
import com.example.inspection.repository.MachineRepository;
import com.example.inspection.repository.ReceiptRepository;
import com.example.inspection.service.MachineService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;
    private final ReceiptRepository receiptRepository;
    private final MachineMapper machineMapper;

    @Override
    public MachineResponse create(MachineRequest request) {
        System.out.println("Creating Machine with request: " + request);
        Receipt receipt = receiptRepository.findById(request.getReceiptId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Receipt not found with id: " + request.getReceiptId()));

        System.out.println("Found Receipt: " + receipt);
        Machine machine = machineMapper.toEntity(request, receipt);
        System.out.println("Creating Machine: " + machine);
        return machineMapper.toResponse(machineRepository.save(machine));
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

        Receipt receipt = receiptRepository.findById(request.getReceiptId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Receipt not found with id: " + request.getReceiptId()));

        machineMapper.updateEntity(machine, request, receipt);
        return machineMapper.toResponse(machineRepository.save(machine));
    }

    @Override
    public void delete(Long id) {
        if (!machineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Machine not found with id: " + id);
        }
        machineRepository.deleteById(id);
    }
}
