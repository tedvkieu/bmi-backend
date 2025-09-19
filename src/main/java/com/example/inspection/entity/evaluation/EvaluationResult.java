package com.example.inspection.entity.evaluation;

import java.math.BigDecimal;
import java.time.LocalDate;

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
// 6. KẾT QUẢ ĐÁNH GIÁ (Có/Không)
// =====================================================
@Entity
@Table(name = "evaluation_results")
@Data
public class EvaluationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @Column(name = "evaluation_id", nullable = false, insertable = false, updatable = false)
    private Long evaluationId;

    @Column(name = "criteria_id", nullable = false)
    private Long criteriaId;

    @Column(name = "checkbox_value")
    private Boolean checkboxValue; // true = Có, false = Không

    @Column(name = "text_value")
    private String textValue;

    @Column(name = "number_value", precision = 10, scale = 2)
    private BigDecimal numberValue;

    @Column(name = "date_value")
    private LocalDate dateValue;

    @Column(name = "select_value")
    private String selectValue;

    @Column(name = "notes")
    private String notes;

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criteria_id", insertable = false, updatable = false)
    private EvaluationCriteria criteria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private InspectionCompletionEvaluation evaluation;
}
