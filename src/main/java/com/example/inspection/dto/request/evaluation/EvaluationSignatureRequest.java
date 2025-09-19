package com.example.inspection.dto.request.evaluation;

import java.time.LocalDate;

import com.example.inspection.entity.evaluation.EvaluationSignature.SignatureType;

import lombok.Data;

@Data
public class EvaluationSignatureRequest {
    private Long evaluationId;
    private SignatureType signatureType;
    private Long userId;
    private LocalDate signatureDate;
    private String fullName;
    private String position;
    private String digitalSignature;
}

