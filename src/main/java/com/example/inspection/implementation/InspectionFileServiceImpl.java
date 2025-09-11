// package com.example.inspection.implementation;

// import com.example.inspection.dto.request.InspectionFileRequest;
// import com.example.inspection.dto.response.InspectionFileResponse;
// import com.example.inspection.entity.Customer;
// //import com.example.inspection.entity.InspectionFile;
// import com.example.inspection.exception.ResourceNotFoundException;
// import com.example.inspection.mapper.InspectionFileMapper;
// import com.example.inspection.repository.CustomerRepository;
// import com.example.inspection.repository.InspectionFileRepository;
// import com.example.inspection.service.InspectionFileService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.stereotype.Service;

// @Service
// @RequiredArgsConstructor
// public class InspectionFileServiceImpl implements InspectionFileService {

// private final InspectionFileRepository inspectionFileRepository;
// private final CustomerRepository customerRepository;
// private final InspectionFileMapper inspectionFileMapper;

// @Override
// public InspectionFileResponse create(InspectionFileRequest request) {
// Customer customer = customerRepository.findById(request.getCustomerId())
// .orElseThrow(
// () -> new ResourceNotFoundException("Customer not found with id " +
// request.getCustomerId()));

// InspectionFile entity = inspectionFileMapper.toEntity(request, customer);
// return inspectionFileMapper.toDto(inspectionFileRepository.save(entity));
// }

// @Override
// public Page<InspectionFileResponse> getAll(int page, int size) {
// return inspectionFileRepository.findAll(PageRequest.of(page, size))
// .map(inspectionFileMapper::toDto);
// }

// @Override
// public InspectionFileResponse getById(Long id) {
// InspectionFile entity = inspectionFileRepository.findById(id)
// .orElseThrow(() -> new ResourceNotFoundException("InspectionFile not found
// with id " + id));
// return inspectionFileMapper.toDto(entity);
// }

// @Override
// public InspectionFileResponse update(Long id, InspectionFileRequest request)
// {
// InspectionFile entity = inspectionFileRepository.findById(id)
// .orElseThrow(() -> new ResourceNotFoundException("InspectionFile not found
// with id " + id));

// Customer customer = customerRepository.findById(request.getCustomerId())
// .orElseThrow(
// () -> new ResourceNotFoundException("Customer not found with id " +
// request.getCustomerId()));

// entity.setCustomer(customer);
// entity.setServiceAddress(request.getServiceAddress());
// entity.setTaxCode(request.getTaxCode());
// entity.setEmail(request.getEmail());
// // entity.setObjectType(request.getObjectType());

// return inspectionFileMapper.toDto(inspectionFileRepository.save(entity));
// }

// @Override
// public void delete(Long id) {
// if (!inspectionFileRepository.existsById(id)) {
// throw new ResourceNotFoundException("InspectionFile not found with id " +
// id);
// }
// inspectionFileRepository.deleteById(id);
// }
// }
