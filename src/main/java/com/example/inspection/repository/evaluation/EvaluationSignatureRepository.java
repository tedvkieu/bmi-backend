package com.example.inspection.repository.evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inspection.entity.evaluation.EvaluationSignature;

@Repository
public interface EvaluationSignatureRepository extends JpaRepository<EvaluationSignature, Long> {
    List<EvaluationSignature> findByEvaluationId(Long evaluationId);
}

