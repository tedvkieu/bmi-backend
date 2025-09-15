package com.example.inspection.mapper;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.ReceiptRequest;
import com.example.inspection.dto.response.ReceiptResponse;
import com.example.inspection.entity.Customer;
import com.example.inspection.entity.Dossier;
import com.example.inspection.entity.InspectionType;

@Component
public class DossierMapper {

    public Dossier toEntity(ReceiptRequest request, Customer customerSubmit, Customer customerRelated,
            InspectionType inspectionType) {
        Dossier dossier = new Dossier();
        dossier.setRegistrationNo(request.getRegistrationNo());
        dossier.setCustomerSubmit(customerSubmit);
        dossier.setCustomerRelated(customerRelated);
        dossier.setInspectionType(inspectionType);
        dossier.setDeclarationNo(request.getDeclarationNo());
        dossier.setBillOfLading(request.getBillOfLading());
        dossier.setShipName(request.getShipName());
        dossier.setCout10(request.getCout10());
        dossier.setCout20(request.getCout20());
        dossier.setBulkShip(request.getBulkShip());
        dossier.setDeclarationDoc(request.getDeclarationDoc());
        dossier.setDeclarationPlace(request.getDeclarationPlace());
        dossier.setInspectionDate(request.getInspectionDate());
        dossier.setCertificateDate(request.getCertificateDate());
        dossier.setInspectionLocation(request.getInspectionLocation());
        dossier.setCertificateStatus(request.getCertificateStatus());
        return dossier;
    }

    public ReceiptResponse toResponse(Dossier entity) {
        ReceiptResponse res = new ReceiptResponse();
        res.setReceiptId(entity.getDossierId());
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
        res.setFiles(entity.getFiles());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        return res;
    }
}
