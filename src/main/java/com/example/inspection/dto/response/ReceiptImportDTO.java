package com.example.inspection.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptImportDTO {
    private Long receiptId;
    private String declarationNo;
    private LocalDate declarationDate;
    private String registrationNo;
    private LocalDate registrationDate;
    private String billOfLading;
    private LocalDate billOfLadingDate;

    private CustomerResponse customer;
    private List<MachineResponse> machines;
}