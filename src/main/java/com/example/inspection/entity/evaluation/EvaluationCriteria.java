package com.example.inspection.entity.evaluation;

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
// 5. TIÊU CHÍ ĐÁNH GIÁ CHI TIẾT
// =====================================================
@Entity
@Table(name = "evaluation_criteria")
@Data
public class EvaluationCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criteria_id")
    private Long criteriaId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "criteria_code", nullable = false)
    private String criteriaCode; // A1, A2, B1, B2...

    @Column(name = "criteria_order")
    private Integer criteriaOrder;

    @Column(name = "criteria_text", nullable = false)
    private String criteriaText; // "Hồ sơ đăng ký ban đầu đầy đủ..."

    @Enumerated(EnumType.STRING)
    @Column(name = "input_type")
    private InputType inputType;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(name = "is_active")
    private Boolean isActive;

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private EvaluationCategory category;

    public enum InputType {
        CHECKBOX, TEXT, SELECT, DATE, NUMBER
    }
}
