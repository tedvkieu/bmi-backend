package com.example.inspection.entity.evaluation;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.inspection.entity.Dossier;

// =====================================================
// 1. BẢNG CHÍNH - PHIẾU ĐÁNH GIÁ
// =====================================================
@Entity
@Table(name = "inspection_completion_evaluations")
@Data
public class InspectionCompletionEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Column(name = "dossier_id", nullable = false)
    private Long dossierId;

    @Column(name = "inspection_number", nullable = false)
    private String inspectionNumber; // BMI/2024/001

    @Column(name = "evaluation_date")
    private LocalDate evaluationDate;

    @Column(name = "evaluator_user_id")
    private Long evaluatorUserId;

    @Column(name = "supervisor_user_id")
    private Long supervisorUserId;

    @Column(name = "status")
    private String status;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships for DOCX generation
    @OneToMany(mappedBy = "evaluation", fetch = FetchType.LAZY)
    private List<EvaluationResult> evaluationResults;

    @OneToMany(mappedBy = "evaluation", fetch = FetchType.LAZY)
    private List<DossierDocumentsChecklist> documentsChecklist;

    @OneToMany(mappedBy = "evaluation", fetch = FetchType.LAZY)
    private List<EvaluationSignature> signatures;

    // Join với dossier để lấy thông tin cơ bản
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id", insertable = false, updatable = false)
    private Dossier dossier;
}
