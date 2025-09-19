package com.example.inspection.entity.evaluation;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "inspection_execution_roles")
@Data
public class InspectionExecutionRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_code", unique = true, nullable = false)
    private String roleCode;

    @Column(name = "role_name", nullable = false)
    private String roleName; // "Trưởng nhóm giám định GĐV"

    @Column(name = "role_description")
    private String roleDescription;

    @Column(name = "is_leader")
    private Boolean isLeader;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}