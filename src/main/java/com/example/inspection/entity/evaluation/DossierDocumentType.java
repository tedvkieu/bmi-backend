package com.example.inspection.entity.evaluation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// =====================================================
// 7. LOẠI TÀI LIỆU
// =====================================================
@Entity
@Table(name = "dossier_document_types")
@Data
public class DossierDocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_type_id")
    private Long documentTypeId;

    @Column(name = "type_code", unique = true, nullable = false)
    private String typeCode;

    @Column(name = "type_name", nullable = false)
    private String typeName; // "Giấy yêu cầu giám định"

    @Column(name = "type_name_english")
    private String typeNameEnglish; // "Request Letter"

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private DocumentCategory category; // CUSTOMER, BMI

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    public enum DocumentCategory {
        CUSTOMER, BMI
    }
}