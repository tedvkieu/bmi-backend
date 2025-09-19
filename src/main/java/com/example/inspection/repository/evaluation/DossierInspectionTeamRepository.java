package com.example.inspection.repository.evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inspection.entity.evaluation.DossierInspectionTeam;

@Repository
public interface DossierInspectionTeamRepository extends JpaRepository<DossierInspectionTeam, Long> {
    List<DossierInspectionTeam> findByDossierId(Long dossierId);
}

