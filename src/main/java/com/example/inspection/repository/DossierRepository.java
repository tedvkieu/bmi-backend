package com.example.inspection.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.inspection.entity.Dossier;

public interface DossierRepository extends JpaRepository<Dossier, Long> {

        @Query("SELECT r FROM Dossier r " +
                        "LEFT JOIN FETCH r.customerRelated " +
                        "LEFT JOIN FETCH r.customerSubmit " +
                        "LEFT JOIN FETCH r.machines " +
                        "WHERE r.dossierId = :dossierId")
        Optional<Dossier> findByIdWithDetails(@Param("dossierId") Long dossierId);

        @Query("SELECT r FROM Dossier r " +
                        "LEFT JOIN FETCH r.customerSubmit " +
                        "LEFT JOIN FETCH r.customerRelated " +
                        "LEFT JOIN FETCH r.inspectionType " +
                        "WHERE r.dossierId = :dossierId")
        Optional<Dossier> findByIdWithCustomers(@Param("dossierId") Long dossierId);

        @Query("SELECT r FROM Dossier r " +
                        "LEFT JOIN FETCH r.machines " +
                        "WHERE r.dossierId = :dossierId")
        Optional<Dossier> findByIdWithMachines(@Param("dossierId") Long dossierId);

        //

}
