package com.example.inspection.dto.response.evaluation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DossierDocumentsChecklistResponse {
    private Long checklistId;
    private Long evaluationId;
    private Long documentTypeId;
    private Boolean hasPhysicalCopy;
    private Boolean hasElectronicCopy;
    private String electronicFilePath;
    private String notes;
}

