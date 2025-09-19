package com.example.inspection.dto.response.evaluation;

import java.time.LocalDate;

import com.example.inspection.entity.evaluation.EvaluationSignature.SignatureType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationSignatureResponse {
    private Long signatureId;
    private Long evaluationId;
    private SignatureType signatureType;
    private Long userId;
    private LocalDate signatureDate;
    private String fullName;
    private String position;
    private String digitalSignature;
}

