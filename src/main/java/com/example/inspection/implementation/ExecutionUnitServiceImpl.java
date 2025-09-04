package com.example.inspection.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.ExecutionUnitRequest;
import com.example.inspection.dto.request.ExecutionUnitResponse;
import com.example.inspection.entity.Employee;
import com.example.inspection.entity.ExecutionUnit;
import com.example.inspection.entity.Receipt;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.mapper.ExecutionUnitMapper;
import com.example.inspection.repository.EmployeeRepository;
import com.example.inspection.repository.ExecutionUnitRepository;
import com.example.inspection.repository.ReceiptRepository;
import com.example.inspection.service.ExecutionUnitService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExecutionUnitServiceImpl implements ExecutionUnitService {

    private final ExecutionUnitRepository executionUnitRepository;
    private final ReceiptRepository receiptRepository;
    private final EmployeeRepository employeeRepository;
    private final ExecutionUnitMapper executionUnitMapper;

    @Override
    public ExecutionUnitResponse create(ExecutionUnitRequest request) {
        Receipt receipt = receiptRepository.findById(request.getReceiptId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Receipt not found with id " + request.getReceiptId()));
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Employee not found with id " + request.getEmployeeId()));

        ExecutionUnit executionUnit = executionUnitMapper.toEntity(request, receipt, employee);
        return executionUnitMapper.toResponse(executionUnitRepository.save(executionUnit));
    }

    @Override
    public ExecutionUnitResponse getById(Long id) {
        ExecutionUnit executionUnit = executionUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExecutionUnit not found with id " + id));
        return executionUnitMapper.toResponse(executionUnit);
    }

    @Override
    public List<ExecutionUnitResponse> getAll() {
        return executionUnitRepository.findAll()
                .stream()
                .map(executionUnitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExecutionUnitResponse update(Long id, ExecutionUnitRequest request) {
        ExecutionUnit executionUnit = executionUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExecutionUnit not found with id " + id));

        Receipt receipt = receiptRepository.findById(request.getReceiptId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Receipt not found with id " + request.getReceiptId()));
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Employee not found with id " + request.getEmployeeId()));

        executionUnitMapper.updateEntity(executionUnit, request, receipt, employee);
        return executionUnitMapper.toResponse(executionUnitRepository.save(executionUnit));
    }

    @Override
    public void delete(Long id) {
        if (!executionUnitRepository.existsById(id)) {
            throw new ResourceNotFoundException("ExecutionUnit not found with id " + id);
        }
        executionUnitRepository.deleteById(id);
    }
}
