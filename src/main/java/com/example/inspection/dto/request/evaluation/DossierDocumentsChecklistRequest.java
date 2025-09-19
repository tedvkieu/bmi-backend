package com.example.inspection.dto.request.evaluation;

import lombok.Data;

@Data
public class DossierDocumentsChecklistRequest {
    private Long evaluationId;
    private Long documentTypeId;
    private Boolean hasPhysicalCopy;
    private Boolean hasElectronicCopy;
    private String electronicFilePath;
    private String notes;
}

