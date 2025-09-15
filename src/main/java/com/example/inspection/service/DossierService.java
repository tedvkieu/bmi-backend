package com.example.inspection.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.ReceiptRequest;
import com.example.inspection.dto.response.ReceiptResponse;
import com.example.inspection.entity.Customer;
import com.example.inspection.entity.Dossier;

public interface DossierService {
    ReceiptResponse createDossier(ReceiptRequest request);

    ReceiptResponse getDossierById(Long id);

    Page<ReceiptResponse> getAll(int page, int size);

    List<ReceiptResponse> getAllDossiers();

    ReceiptResponse updateDossier(Long id, ReceiptRequest request);

    void deleteDossier(Long id);

    Dossier createDossierFromSheet(Sheet sheet, Customer customer);
}
