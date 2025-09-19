package com.example.inspection.dto.response.evaluation;

import com.example.inspection.entity.evaluation.DossierDocumentType.DocumentCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DossierDocumentTypeResponse {
    private Long documentTypeId;
    private String typeCode;
    private String typeName;
    private String typeNameEnglish;
    private DocumentCategory category;
    private Boolean isRequired;
    private Integer displayOrder;
    private Boolean isActive;
}

