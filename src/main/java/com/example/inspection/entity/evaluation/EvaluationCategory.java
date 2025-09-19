package com.example.inspection.entity.evaluation;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

// =====================================================
// 4. DANH MỤC ĐÁNH GIÁ (A, B, C, D)
// =====================================================
@Entity
@Table(name = "evaluation_categories")
@Data
public class EvaluationCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_code", unique = true, nullable = false)
    private String categoryCode; // DOSSIER_REVIEW, PREPARATION, PROCESSING, CERTIFICATION

    @Column(name = "category_name", nullable = false)
    private String categoryName; // "A. Xem xét sự đầy đủ thành phần hồ sơ..."

    @Column(name = "category_order")
    private Integer categoryOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    // Relationship
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<EvaluationCriteria> criteria;
}
