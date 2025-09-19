package com.example.inspection.repository.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inspection.entity.evaluation.DossierDocumentType;

@Repository
public interface DossierDocumentTypeRepository extends JpaRepository<DossierDocumentType, Long> {
}

