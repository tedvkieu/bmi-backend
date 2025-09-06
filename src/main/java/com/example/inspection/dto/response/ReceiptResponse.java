package com.example.inspection.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.inspection.entity.Receipt.CertificateStatus;

@Data
public class ReceiptResponse {
    private Long receiptId;
    private String registrationNo;
    private Long customerSubmitId;
    private Long customerRelatedId;
    private String inspectionTypeId;
    private String declarationNo;
    private String billOfLading;
    private String shipName;
    private Integer cout10;
    private Integer cout20;
    private Boolean bulkShip;
    private String declarationDoc;
    private String declarationPlace;
    private LocalDate inspectionDate;
    private LocalDate certificateDate;
    private String inspectionLocation;
    private CertificateStatus certificateStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
