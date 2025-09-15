package com.example.inspection.dto.request;

import lombok.Data;

@Data
public class MachineRequest {
    private Long dossierId; // liên kết Receipt
    private String registrationNo;
    private String itemName;
    private String brand;
    private String model;
    private String serialNumber;
    private String manufactureCountry;
    private String manufacturerName;
    private Integer manufactureYear;
    private Integer quantity;
    private String usage;
    private String note;
}
