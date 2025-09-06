package com.example.inspection.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inspection.entity.Machine;

public interface MachineRepository extends JpaRepository<Machine, Long> {
}
