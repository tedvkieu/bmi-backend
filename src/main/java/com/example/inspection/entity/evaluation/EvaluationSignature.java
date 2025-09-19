package com.example.inspection.entity.evaluation;

import java.time.LocalDate;

import com.example.inspection.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

// =====================================================
// 9. CHỮ KÝ DUYỆT
// =====================================================
@Entity
@Table(name = "evaluation_signatures")
@Data
public class EvaluationSignature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "signature_id")
    private Long signatureId;

    @Column(name = "evaluation_id", nullable = false, insertable = false, updatable = false)
    private Long evaluationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "signature_type", nullable = false)
    private SignatureType signatureType; // EVALUATOR, SUPERVISOR

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "signature_date")
    private LocalDate signatureDate; // Ngày ký

    @Column(name = "full_name")
    private String fullName; // Họ tên người ký

    @Column(name = "position")
    private String position; // Chức vụ

    @Column(name = "digital_signature")
    private String digitalSignature;

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private InspectionCompletionEvaluation evaluation;

    public enum SignatureType {
        EVALUATOR, // Người theo dõi
        SUPERVISOR // Lãnh đạo
    }
}
