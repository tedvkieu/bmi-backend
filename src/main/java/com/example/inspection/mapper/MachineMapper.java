// MachineMapper.java
package com.example.inspection.mapper;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.MachineRequest;
import com.example.inspection.dto.response.MachineResponse;
import com.example.inspection.entity.Machine;
import com.example.inspection.entity.Receipt;

@Component
public class MachineMapper {

    public Machine toEntity(MachineRequest request, Receipt receipt) {
        Machine machine = new Machine();
        machine.setReceipt(receipt);
        machine.setRegistrationNo(request.getRegistrationNo());
        machine.setItemName(request.getItemName());
        machine.setBrand(request.getBrand());
        machine.setModel(request.getModel());
        machine.setSerialNumber(request.getSerialNumber());
        machine.setManufactureCountry(request.getManufactureCountry());
        machine.setManufacturerName(request.getManufacturerName());
        machine.setManufactureYear(request.getManufactureYear());
        machine.setQuantity(request.getQuantity());
        machine.setUsage(request.getUsage());
        machine.setNote(request.getNote());
        return machine;
    }

    public void updateEntity(Machine machine, MachineRequest request, Receipt receipt) {
        machine.setReceipt(receipt);
        machine.setRegistrationNo(request.getRegistrationNo());
        machine.setItemName(request.getItemName());
        machine.setBrand(request.getBrand());
        machine.setModel(request.getModel());
        machine.setSerialNumber(request.getSerialNumber());
        machine.setManufactureCountry(request.getManufactureCountry());
        machine.setManufacturerName(request.getManufacturerName());
        machine.setManufactureYear(request.getManufactureYear());
        machine.setQuantity(request.getQuantity());
        machine.setUsage(request.getUsage());
        machine.setNote(request.getNote());
    }

    public MachineResponse toResponse(Machine machine) {
        MachineResponse response = new MachineResponse();
        response.setMachineId(machine.getMachineId());
        response.setReceiptId(machine.getReceipt().getReceiptId());
        response.setRegistrationNo(machine.getRegistrationNo());
        response.setItemName(machine.getItemName());
        response.setBrand(machine.getBrand());
        response.setModel(machine.getModel());
        response.setSerialNumber(machine.getSerialNumber());
        response.setManufactureCountry(machine.getManufactureCountry());
        response.setManufacturerName(machine.getManufacturerName());
        response.setManufactureYear(machine.getManufactureYear());
        response.setQuantity(machine.getQuantity());
        response.setUsage(machine.getUsage());
        response.setNote(machine.getNote());
        response.setCreatedAt(machine.getCreatedAt());
        response.setUpdatedAt(machine.getUpdatedAt());
        return response;
    }
}
