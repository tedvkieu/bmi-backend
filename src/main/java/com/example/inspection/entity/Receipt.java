package com.example.inspection.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 5. Receipt Entity
@Entity
@Table(name = "receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {
        "customerSubmit",
        "customerRelated",
        "inspectionType",
        "executionUnits",
        "machines",
        "inspectionResults"
})
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long receiptId;

    @Column(name = "registration_no", length = 100)
    private String registrationNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_submit_id", nullable = false)
    private Customer customerSubmit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_related_id")
    private Customer customerRelated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_type_id", nullable = false)
    private InspectionType inspectionType;

    @Column(name = "declaration_no", length = 100)
    private String declarationNo;

    @Column(name = "bill_of_lading", length = 100)
    private String billOfLading;

    @Column(name = "ship_name", length = 255)
    private String shipName;

    @Column(name = "cout10")
    private Integer cout10;

    @Column(name = "cout20")
    private Integer cout20;

    @Column(name = "bulk_ship", nullable = false)
    private Boolean bulkShip = false;

    @Column(name = "declaration_doc", length = 500)
    private String declarationDoc;

    @Column(name = "declaration_place", length = 255)
    private String declarationPlace;

    @Column(name = "inspection_date")
    private LocalDate inspectionDate;

    @Column(name = "certificate_date")
    private LocalDate certificateDate;

    @Column(name = "inspection_location", length = 500)
    private String inspectionLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "certificate_status", nullable = false)
    private CertificateStatus certificateStatus = CertificateStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExecutionUnit> executionUnits;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Machine> machines;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InspectionResult> inspectionResults;

    public enum CertificateStatus {
        PENDING("Đang xử lý"),
        APPROVED("Đã duyệt"),
        REJECTED("Từ chối");

        private final String description;

        CertificateStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}