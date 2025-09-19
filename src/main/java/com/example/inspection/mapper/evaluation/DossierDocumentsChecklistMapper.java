package com.example.inspection.mapper.evaluation;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.evaluation.DossierDocumentsChecklistRequest;
import com.example.inspection.dto.response.evaluation.DossierDocumentsChecklistResponse;
import com.example.inspection.entity.evaluation.DossierDocumentsChecklist;

@Component
public class DossierDocumentsChecklistMapper {

    public DossierDocumentsChecklist toEntity(DossierDocumentsChecklistRequest request) {
        DossierDocumentsChecklist entity = new DossierDocumentsChecklist();
        entity.setEvaluationId(request.getEvaluationId());
        entity.setDocumentTypeId(request.getDocumentTypeId());
        entity.setHasPhysicalCopy(request.getHasPhysicalCopy());
        entity.setHasElectronicCopy(request.getHasElectronicCopy());
        entity.setElectronicFilePath(request.getElectronicFilePath());
        entity.setNotes(request.getNotes());
        return entity;
    }

    public void updateEntity(DossierDocumentsChecklist entity, DossierDocumentsChecklistRequest request) {
        entity.setEvaluationId(request.getEvaluationId());
        entity.setDocumentTypeId(request.getDocumentTypeId());
        entity.setHasPhysicalCopy(request.getHasPhysicalCopy());
        entity.setHasElectronicCopy(request.getHasElectronicCopy());
        entity.setElectronicFilePath(request.getElectronicFilePath());
        entity.setNotes(request.getNotes());
    }

    public DossierDocumentsChecklistResponse toResponse(DossierDocumentsChecklist entity) {
        return new DossierDocumentsChecklistResponse(
                entity.getChecklistId(),
                entity.getEvaluationId(),
                entity.getDocumentTypeId(),
                entity.getHasPhysicalCopy(),
                entity.getHasElectronicCopy(),
                entity.getElectronicFilePath(),
                entity.getNotes());
    }
}

