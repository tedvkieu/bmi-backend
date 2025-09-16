package com.example.inspection.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import com.example.inspection.entity.Dossier.CertificateStatus;

@Data
public class ReceiptRequest {
    private String registrationNo;
    @NotNull(message = "customerSubmitId is required")
    private Long customerSubmitId;
    private Long customerRelatedId;
    @NotNull(message = "inspectionTypeId is required")
    private String inspectionTypeId;
    private String declarationNo;
    private String billOfLading;
    private String shipName;
    private Integer cout10;
    private Integer cout20;
    @NotNull(message = "bulkShip is required")
    private Boolean bulkShip;
    private String declarationDoc;
    private String declarationPlace;
    private LocalDate inspectionDate;
    private LocalDate certificateDate;
    private String inspectionLocation;
    private CertificateStatus certificateStatus;
}
