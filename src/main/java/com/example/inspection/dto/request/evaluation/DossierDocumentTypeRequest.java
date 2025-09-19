package com.example.inspection.dto.request.evaluation;

import com.example.inspection.entity.evaluation.DossierDocumentType.DocumentCategory;

import lombok.Data;

@Data
public class DossierDocumentTypeRequest {
    private String typeCode;
    private String typeName;
    private String typeNameEnglish;
    private DocumentCategory category;
    private Boolean isRequired;
    private Integer displayOrder;
    private Boolean isActive;
}

