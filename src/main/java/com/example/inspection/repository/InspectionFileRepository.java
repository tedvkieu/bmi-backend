package com.example.inspection.repository;

import com.example.inspection.entity.InspectionFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionFileRepository extends JpaRepository<InspectionFile, Long> {
}
