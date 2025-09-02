package com.example.inspection.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 8. InspectionResult Entity
@Entity
@Table(name = "inspection_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_result_id")
    private Long inspectionResultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    // Snapshot fields from Machine at inspection time
    @Column(name = "snapshot_registration_no", length = 100)
    private String snapshotRegistrationNo;

    @Column(name = "snapshot_item_name", length = 255)
    private String snapshotItemName;

    @Column(name = "snapshot_brand", length = 100)
    private String snapshotBrand;

    @Column(name = "snapshot_model", length = 100)
    private String snapshotModel;

    @Column(name = "snapshot_serial_number", length = 100)
    private String snapshotSerialNumber;

    @Column(name = "snapshot_manufacture_country", length = 100)
    private String snapshotManufactureCountry;

    @Column(name = "snapshot_manufacturer_name", length = 255)
    private String snapshotManufacturerName;

    @Column(name = "snapshot_manufacture_year")
    private Integer snapshotManufactureYear;

    @Column(name = "snapshot_quantity")
    private Integer snapshotQuantity;

    @Column(name = "snapshot_usage", columnDefinition = "TEXT")
    private String snapshotUsage;

    @Column(name = "evaluation", columnDefinition = "TEXT")
    private String evaluation;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
