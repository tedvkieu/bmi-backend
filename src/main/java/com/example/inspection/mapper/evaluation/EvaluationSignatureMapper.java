package com.example.inspection.mapper.evaluation;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.evaluation.EvaluationSignatureRequest;
import com.example.inspection.dto.response.evaluation.EvaluationSignatureResponse;
import com.example.inspection.entity.evaluation.EvaluationSignature;

@Component
public class EvaluationSignatureMapper {

    public EvaluationSignature toEntity(EvaluationSignatureRequest request) {
        EvaluationSignature entity = new EvaluationSignature();
        entity.setEvaluationId(request.getEvaluationId());
        entity.setSignatureType(request.getSignatureType());
        entity.setUserId(request.getUserId());
        entity.setSignatureDate(request.getSignatureDate());
        entity.setFullName(request.getFullName());
        entity.setPosition(request.getPosition());
        entity.setDigitalSignature(request.getDigitalSignature());
        return entity;
    }

    public void updateEntity(EvaluationSignature entity, EvaluationSignatureRequest request) {
        entity.setEvaluationId(request.getEvaluationId());
        entity.setSignatureType(request.getSignatureType());
        entity.setUserId(request.getUserId());
        entity.setSignatureDate(request.getSignatureDate());
        entity.setFullName(request.getFullName());
        entity.setPosition(request.getPosition());
        entity.setDigitalSignature(request.getDigitalSignature());
    }

    public EvaluationSignatureResponse toResponse(EvaluationSignature entity) {
        return new EvaluationSignatureResponse(
                entity.getSignatureId(),
                entity.getEvaluationId(),
                entity.getSignatureType(),
                entity.getUserId(),
                entity.getSignatureDate(),
                entity.getFullName(),
                entity.getPosition(),
                entity.getDigitalSignature());
    }
}

