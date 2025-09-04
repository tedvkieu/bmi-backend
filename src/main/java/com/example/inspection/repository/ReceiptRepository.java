package com.example.inspection.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inspection.entity.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
