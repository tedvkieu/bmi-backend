package com.example.inspection.repository.evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inspection.entity.evaluation.DossierDocumentsChecklist;

@Repository
public interface DossierDocumentsChecklistRepository extends JpaRepository<DossierDocumentsChecklist, Long> {
    List<DossierDocumentsChecklist> findByEvaluationId(Long evaluationId);
}

