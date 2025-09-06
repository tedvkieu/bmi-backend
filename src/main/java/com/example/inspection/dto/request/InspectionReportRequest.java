package com.example.inspection.dto.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionReportRequest {
    private Long receiptId;
    private String templateName;
}
