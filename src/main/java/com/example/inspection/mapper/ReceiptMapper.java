package com.example.inspection.mapper;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.ReceiptRequest;
import com.example.inspection.dto.response.ReceiptResponse;
import com.example.inspection.entity.Customer;
import com.example.inspection.entity.InspectionType;
import com.example.inspection.entity.Receipt;

@Component
public class ReceiptMapper {

    public Receipt toEntity(ReceiptRequest request, Customer customerSubmit, Customer customerRelated,
            InspectionType inspectionType) {
        Receipt receipt = new Receipt();
        receipt.setRegistrationNo(request.getRegistrationNo());
        receipt.setCustomerSubmit(customerSubmit);
        receipt.setCustomerRelated(customerRelated);
        receipt.setInspectionType(inspectionType);
        receipt.setDeclarationNo(request.getDeclarationNo());
        receipt.setBillOfLading(request.getBillOfLading());
        receipt.setShipName(request.getShipName());
        receipt.setCout10(request.getCout10());
        receipt.setCout20(request.getCout20());
        receipt.setBulkShip(request.getBulkShip());
        receipt.setDeclarationDoc(request.getDeclarationDoc());
        receipt.setDeclarationPlace(request.getDeclarationPlace());
        receipt.setInspectionDate(request.getInspectionDate());
        receipt.setCertificateDate(request.getCertificateDate());
        receipt.setInspectionLocation(request.getInspectionLocation());
        receipt.setCertificateStatus(request.getCertificateStatus());
        return receipt;
    }

    public ReceiptResponse toResponse(Receipt entity) {
        ReceiptResponse res = new ReceiptResponse();
        res.setReceiptId(entity.getReceiptId());
        res.setRegistrationNo(entity.getRegistrationNo());
        res.setCustomerSubmitId(entity.getCustomerSubmit().getCustomerId());
        res.setCustomerRelatedId(
                entity.getCustomerRelated() != null ? entity.getCustomerRelated().getCustomerId() : null);
        res.setInspectionTypeId(entity.getInspectionType().getInspectionTypeId());
        res.setDeclarationNo(entity.getDeclarationNo());
        res.setBillOfLading(entity.getBillOfLading());
        res.setShipName(entity.getShipName());
        res.setCout10(entity.getCout10());
        res.setCout20(entity.getCout20());
        res.setBulkShip(entity.getBulkShip());
        res.setDeclarationDoc(entity.getDeclarationDoc());
        res.setDeclarationPlace(entity.getDeclarationPlace());
        res.setInspectionDate(entity.getInspectionDate());
        res.setCertificateDate(entity.getCertificateDate());
        res.setInspectionLocation(entity.getInspectionLocation());
        res.setCertificateStatus(entity.getCertificateStatus());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        return res;
    }
}
