package com.example.inspection.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import com.example.inspection.dto.request.MachineRequest;
import com.example.inspection.dto.response.MachineResponse;

public interface MachineService {
    MachineResponse create(MachineRequest request);

    MachineResponse getById(Long id);

    List<MachineResponse> getAll();

    MachineResponse update(Long id, MachineRequest request);

    void delete(Long id);

    void importMachinesFromSheet(Sheet sheet, Long receiptId);
}
