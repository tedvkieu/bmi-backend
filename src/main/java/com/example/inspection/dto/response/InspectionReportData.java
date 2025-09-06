package com.example.inspection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionReportData {
    // Customer information
    private String importerName;
    private String address;
    private String taxCode;
    private String phone;
    private String email;

    // Receipt information
    private String billOfLading;
    private String declarationNo;
    private String registrationNo;

    // Machine information (first machine from the receipt)
    private String itemName;
    private String brand;
    private String model;
    private String serialNumber;
    private String manufactureYear;
    private String manufacturerName;
    private String manufactureCountry;
    private String quantity;

    // Additional fields for the document
    private String currentDate;
    private String inspectionLocation;
    private String inspectionDate;
}