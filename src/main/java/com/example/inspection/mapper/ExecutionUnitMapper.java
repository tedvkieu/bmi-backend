package com.example.inspection.mapper;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.ExecutionUnitRequest;
import com.example.inspection.dto.request.ExecutionUnitResponse;
import com.example.inspection.entity.Employee;
import com.example.inspection.entity.ExecutionUnit;
import com.example.inspection.entity.Receipt;

@Component
public class ExecutionUnitMapper {

    public ExecutionUnit toEntity(ExecutionUnitRequest request, Receipt receipt, Employee employee) {
        ExecutionUnit executionUnit = new ExecutionUnit();
        executionUnit.setReceipt(receipt);
        executionUnit.setEmployee(employee);
        executionUnit.setRoleInCase(request.getRoleInCase());
        return executionUnit;
    }

    public void updateEntity(ExecutionUnit executionUnit, ExecutionUnitRequest request, Receipt receipt,
            Employee employee) {
        executionUnit.setReceipt(receipt);
        executionUnit.setEmployee(employee);
        executionUnit.setRoleInCase(request.getRoleInCase());
    }

    public ExecutionUnitResponse toResponse(ExecutionUnit executionUnit) {
        ExecutionUnitResponse response = new ExecutionUnitResponse();
        response.setExecutionUnitId(executionUnit.getExecutionUnitId());
        response.setReceiptId(executionUnit.getReceipt().getReceiptId());
        response.setEmployeeId(executionUnit.getEmployee().getEmployeeId());
        response.setRoleInCase(executionUnit.getRoleInCase());
        response.setCreatedAt(executionUnit.getCreatedAt());
        response.setUpdatedAt(executionUnit.getUpdatedAt());
        return response;
    }
}
