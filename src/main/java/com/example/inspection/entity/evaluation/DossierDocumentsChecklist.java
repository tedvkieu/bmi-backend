package com.example.inspection.entity.evaluation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

// =====================================================
// 8. DANH SÁCH KIỂM TRA TÀI LIỆU
// =====================================================
@Entity
@Table(name = "dossier_documents_checklist")
@Data
public class DossierDocumentsChecklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_id")
    private Long checklistId;

    @Column(name = "evaluation_id", nullable = false, insertable = false, updatable = false)
    private Long evaluationId;

    @Column(name = "document_type_id", nullable = false)
    private Long documentTypeId;

    @Column(name = "has_physical_copy")
    private Boolean hasPhysicalCopy; // Có hồ sơ giấy

    @Column(name = "has_electronic_copy")
    private Boolean hasElectronicCopy; // Có hồ sơ điện tử

    @Column(name = "electronic_file_path")
    private String electronicFilePath;

    @Column(name = "notes")
    private String notes;

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", insertable = false, updatable = false)
    private DossierDocumentType documentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private InspectionCompletionEvaluation evaluation;
}
