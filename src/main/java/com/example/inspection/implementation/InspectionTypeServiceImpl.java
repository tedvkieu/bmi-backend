package com.example.inspection.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.InspectionTypeRequest;
import com.example.inspection.dto.response.InspectionTypeResponse;
import com.example.inspection.entity.InspectionType;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.mapper.InspectionTypeMapper;
import com.example.inspection.repository.InspectionTypeRepository;
import com.example.inspection.service.InspectionTypeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InspectionTypeServiceImpl implements InspectionTypeService {

    private final InspectionTypeRepository inspectionTypeRepository;
    private final InspectionTypeMapper inspectionTypeMapper;

    @Override
    public InspectionTypeResponse create(InspectionTypeRequest request) {
        InspectionType inspectionType = inspectionTypeMapper.toEntity(request);
        return inspectionTypeMapper.toResponse(inspectionTypeRepository.save(inspectionType));
    }

    @Override
    public InspectionTypeResponse getById(String id) {
        InspectionType inspectionType = inspectionTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InspectionType not found with id: " + id));
        return inspectionTypeMapper.toResponse(inspectionType);
    }

    @Override
    public List<InspectionTypeResponse> getAll() {
        return inspectionTypeRepository.findAll()
                .stream()
                .map(inspectionTypeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InspectionTypeResponse update(String id, InspectionTypeRequest request) {
        InspectionType inspectionType = inspectionTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InspectionType not found with id: " + id));
        inspectionTypeMapper.updateEntity(inspectionType, request);
        return inspectionTypeMapper.toResponse(inspectionTypeRepository.save(inspectionType));
    }

    @Override
    public void delete(String id) {
        if (!inspectionTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("InspectionType not found with id: " + id);
        }
        inspectionTypeRepository.deleteById(id);
    }
}
