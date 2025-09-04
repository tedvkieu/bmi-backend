package com.example.inspection.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.ReceiptRequest;
import com.example.inspection.dto.response.ReceiptResponse;

public interface ReceiptService {
    ReceiptResponse createReceipt(ReceiptRequest request);

    ReceiptResponse getReceiptById(Long id);

    Page<ReceiptResponse> getAll(int page, int size);

    List<ReceiptResponse> getAllReceipts();

    ReceiptResponse updateReceipt(Long id, ReceiptRequest request);

    void deleteReceipt(Long id);
}
