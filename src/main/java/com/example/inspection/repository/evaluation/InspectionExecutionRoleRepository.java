package com.example.inspection.repository.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inspection.entity.evaluation.InspectionExecutionRole;

@Repository
public interface InspectionExecutionRoleRepository extends JpaRepository<InspectionExecutionRole, Long> {
}

