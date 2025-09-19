package com.example.inspection.repository.evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inspection.entity.evaluation.InspectionCompletionEvaluation;

@Repository
public interface InspectionCompletionEvaluationRepository extends JpaRepository<InspectionCompletionEvaluation, Long> {
    List<InspectionCompletionEvaluation> findByDossierId(Long dossierId);
}

