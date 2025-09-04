package com.example.inspection.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.ReceiptRequest;
import com.example.inspection.dto.response.ReceiptResponse;
import com.example.inspection.entity.Customer;
import com.example.inspection.entity.InspectionType;
import com.example.inspection.entity.Receipt;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.mapper.ReceiptMapper;
import com.example.inspection.repository.CustomerRepository;
import com.example.inspection.repository.InspectionTypeRepository;
import com.example.inspection.repository.ReceiptRepository;
import com.example.inspection.service.ReceiptService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

        private final ReceiptRepository receiptRepository;
        private final CustomerRepository customerRepository;
        private final InspectionTypeRepository inspectionTypeRepository;
        private final ReceiptMapper receiptMapper;

        @Override
        public ReceiptResponse createReceipt(ReceiptRequest request) {
                Customer submit = customerRepository.findById(request.getCustomerSubmitId())
                                .orElseThrow(() -> new ResourceNotFoundException("Customer submit not found"));
                Customer related = request.getCustomerRelatedId() != null
                                ? customerRepository.findById(request.getCustomerRelatedId())
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Customer related not found"))
                                : null;
                InspectionType inspectionType = inspectionTypeRepository
                                .findByInspectionTypeId(request.getInspectionTypeId())
                                .orElseThrow(() -> new ResourceNotFoundException("InspectionType not found"));

                Receipt receipt = receiptMapper.toEntity(request, submit, related, inspectionType);
                return receiptMapper.toResponse(receiptRepository.save(receipt));
        }

        @Override
        public ReceiptResponse getReceiptById(Long id) {
                Receipt receipt = receiptRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found"));
                return receiptMapper.toResponse(receipt);
        }

        @Override
        public Page<ReceiptResponse> getAll(int page, int size) {
                Pageable pageable = PageRequest.of(page, size);
                return receiptRepository.findAll(pageable).map(receiptMapper::toResponse);
        }

        @Override
        public List<ReceiptResponse> getAllReceipts() {
                return receiptRepository.findAll()
                                .stream()
                                .map(receiptMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public ReceiptResponse updateReceipt(Long id, ReceiptRequest request) {
                Receipt receipt = receiptRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found"));

                Customer submit = customerRepository.findById(request.getCustomerSubmitId())
                                .orElseThrow(() -> new ResourceNotFoundException("Customer submit not found"));
                Customer related = request.getCustomerRelatedId() != null
                                ? customerRepository.findById(request.getCustomerRelatedId())
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Customer related not found"))
                                : null;
                InspectionType inspectionType = inspectionTypeRepository
                                .findByInspectionTypeId(request.getInspectionTypeId())
                                .orElseThrow(() -> new ResourceNotFoundException("InspectionType not found"));

                Receipt updated = receiptMapper.toEntity(request, submit, related, inspectionType);
                updated.setReceiptId(receipt.getReceiptId());
                return receiptMapper.toResponse(receiptRepository.save(updated));
        }

        @Override
        public void deleteReceipt(Long id) {
                Receipt receipt = receiptRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found"));
                receiptRepository.delete(receipt);
        }
}
