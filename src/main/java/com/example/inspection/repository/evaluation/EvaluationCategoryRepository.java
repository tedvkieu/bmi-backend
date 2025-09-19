package com.example.inspection.repository.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inspection.entity.evaluation.EvaluationCategory;

@Repository
public interface EvaluationCategoryRepository extends JpaRepository<EvaluationCategory, Long> {
}

