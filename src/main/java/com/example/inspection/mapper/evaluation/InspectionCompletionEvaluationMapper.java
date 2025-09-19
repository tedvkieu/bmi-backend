package com.example.inspection.mapper.evaluation;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.evaluation.InspectionCompletionEvaluationRequest;
import com.example.inspection.dto.response.evaluation.InspectionCompletionEvaluationResponse;
import com.example.inspection.entity.evaluation.InspectionCompletionEvaluation;

@Component
public class InspectionCompletionEvaluationMapper {

    public InspectionCompletionEvaluation toEntity(InspectionCompletionEvaluationRequest request) {
        InspectionCompletionEvaluation entity = new InspectionCompletionEvaluation();
        entity.setDossierId(request.getDossierId());
        entity.setInspectionNumber(request.getInspectionNumber());
        entity.setEvaluationDate(request.getEvaluationDate());
        entity.setEvaluatorUserId(request.getEvaluatorUserId());
        entity.setSupervisorUserId(request.getSupervisorUserId());
        entity.setStatus(request.getStatus());
        entity.setNotes(request.getNotes());
        return entity;
    }

    public void updateEntity(InspectionCompletionEvaluation entity, InspectionCompletionEvaluationRequest request) {
        entity.setDossierId(request.getDossierId());
        entity.setInspectionNumber(request.getInspectionNumber());
        entity.setEvaluationDate(request.getEvaluationDate());
        entity.setEvaluatorUserId(request.getEvaluatorUserId());
        entity.setSupervisorUserId(request.getSupervisorUserId());
        entity.setStatus(request.getStatus());
        entity.setNotes(request.getNotes());
    }

    public InspectionCompletionEvaluationResponse toResponse(InspectionCompletionEvaluation entity) {
        return new InspectionCompletionEvaluationResponse(
                entity.getEvaluationId(),
                entity.getDossierId(),
                entity.getInspectionNumber(),
                entity.getEvaluationDate(),
                entity.getEvaluatorUserId(),
                entity.getSupervisorUserId(),
                entity.getStatus(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

