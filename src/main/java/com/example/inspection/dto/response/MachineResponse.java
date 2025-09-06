package com.example.inspection.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MachineResponse {
    private Long machineId;
    private Long receiptId;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
