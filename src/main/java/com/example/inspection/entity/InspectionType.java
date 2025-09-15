package com.example.inspection.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 2. InspectionType Entity
@Entity
@Table(name = "inspection_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionType {
    @Id
    @Column(name = "inspection_type_id")
    private String inspectionTypeId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "inspectionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dossier> dossiers;
}