package com.example.inspection.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "tax_code", length = 50)
    private String taxCode;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerType customerType;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "customerSubmit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dossier> submittedDossiers;

    @OneToMany(mappedBy = "customerRelated", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dossier> relatedDossiers;

    // @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch =
    // FetchType.LAZY)
    // private List<InspectionFile> inspectionFiles;

    public enum CustomerType {
        IMPORTER("Người nhập khẩu"),
        SERVICE_MANAGER("Quản lý dịch vụ");

        private final String description;

        CustomerType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

}