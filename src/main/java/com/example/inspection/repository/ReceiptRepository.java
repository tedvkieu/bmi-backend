package com.example.inspection.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.inspection.entity.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query("SELECT r FROM Receipt r " +
            "LEFT JOIN FETCH r.customerRelated " +
            "LEFT JOIN FETCH r.customerSubmit " +
            "LEFT JOIN FETCH r.machines " +
            "WHERE r.receiptId = :receiptId")
    Optional<Receipt> findByIdWithDetails(@Param("receiptId") Long receiptId);
}
