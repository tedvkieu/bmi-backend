package com.example.inspection.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inspection.entity.Machine;

public interface MachineRepository extends JpaRepository<Machine, Long> {

    Optional<Machine> findByMachineId(Long id);

    List<Machine> findByDossier_DossierId(Long dossierId);
}
