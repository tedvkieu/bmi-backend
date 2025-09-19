package com.example.inspection.entity.evaluation;

import java.time.LocalDate;

import com.example.inspection.entity.User;
import com.example.inspection.entity.Dossier;

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
// 2. TEAM GIÁM ĐỊNH
// =====================================================
@Entity
@Table(name = "dossier_inspection_team")
@Data
public class DossierInspectionTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "dossier_id", nullable = false)
    private Long dossierId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "execution_role_id", nullable = false)
    private Long executionRoleId;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    @Column(name = "assigned_task")
    private String assignedTask;

    @Column(name = "is_active")
    private Boolean isActive;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_role_id", insertable = false, updatable = false)
    private InspectionExecutionRole executionRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id", insertable = false, updatable = false)
    private Dossier dossier;
}
