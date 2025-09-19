package com.example.inspection.mapper.evaluation;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.evaluation.DossierDocumentTypeRequest;
import com.example.inspection.dto.response.evaluation.DossierDocumentTypeResponse;
import com.example.inspection.entity.evaluation.DossierDocumentType;

@Component
public class DossierDocumentTypeMapper {

    public DossierDocumentType toEntity(DossierDocumentTypeRequest request) {
        DossierDocumentType entity = new DossierDocumentType();
        entity.setTypeCode(request.getTypeCode());
        entity.setTypeName(request.getTypeName());
        entity.setTypeNameEnglish(request.getTypeNameEnglish());
        entity.setCategory(request.getCategory());
        entity.setIsRequired(request.getIsRequired());
        entity.setDisplayOrder(request.getDisplayOrder());
        entity.setIsActive(request.getIsActive());
        return entity;
    }

    public void updateEntity(DossierDocumentType entity, DossierDocumentTypeRequest request) {
        entity.setTypeCode(request.getTypeCode());
        entity.setTypeName(request.getTypeName());
        entity.setTypeNameEnglish(request.getTypeNameEnglish());
        entity.setCategory(request.getCategory());
        entity.setIsRequired(request.getIsRequired());
        entity.setDisplayOrder(request.getDisplayOrder());
        entity.setIsActive(request.getIsActive());
    }

    public DossierDocumentTypeResponse toResponse(DossierDocumentType entity) {
        return new DossierDocumentTypeResponse(
                entity.getDocumentTypeId(),
                entity.getTypeCode(),
                entity.getTypeName(),
                entity.getTypeNameEnglish(),
                entity.getCategory(),
                entity.getIsRequired(),
                entity.getDisplayOrder(),
                entity.getIsActive());
    }
}

