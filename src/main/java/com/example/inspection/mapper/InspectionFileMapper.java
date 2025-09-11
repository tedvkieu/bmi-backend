// package com.example.inspection.mapper;

// import com.example.inspection.dto.request.InspectionFileRequest;
// import com.example.inspection.dto.response.InspectionFileResponse;
// import com.example.inspection.entity.Customer;
// import com.example.inspection.entity.InspectionFile;
// import org.springframework.stereotype.Component;

// @Component
// public class InspectionFileMapper {

// public InspectionFile toEntity(InspectionFileRequest request, Customer
// customer) {
// InspectionFile entity = new InspectionFile();
// entity.setCustomer(customer);
// entity.setServiceAddress(request.getServiceAddress());
// entity.setTaxCode(request.getTaxCode());
// entity.setEmail(request.getEmail());
// // entity.setObjectType(request.getObjectType());
// return entity;
// }

// public InspectionFileResponse toDto(InspectionFile entity) {
// return InspectionFileResponse.builder()
// .inspectionFileId(entity.getInspectionFileId())
// .customerId(entity.getCustomer().getCustomerId())
// .customerName(entity.getCustomer().getName())
// .serviceAddress(entity.getServiceAddress())
// .taxCode(entity.getTaxCode())
// .email(entity.getEmail())
// // .objectType(entity.getObjectType().name())
// .createdAt(entity.getCreatedAt())
// .updatedAt(entity.getUpdatedAt())
// .build();
// }
// }
