package com.example.inspection.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inspection.entity.InspectionType;

public interface InspectionTypeRepository extends JpaRepository<InspectionType, String> {
    Optional<InspectionType> findByInspectionTypeId(String id);

    // Optional<InspectionType> findById(Long id);
}
