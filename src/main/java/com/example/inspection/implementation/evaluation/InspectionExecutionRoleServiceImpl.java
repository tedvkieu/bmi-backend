package com.example.inspection.implementation.evaluation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.evaluation.InspectionExecutionRoleRequest;
import com.example.inspection.dto.response.evaluation.InspectionExecutionRoleResponse;
import com.example.inspection.entity.evaluation.InspectionExecutionRole;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.repository.evaluation.InspectionExecutionRoleRepository;
import com.example.inspection.mapper.evaluation.InspectionExecutionRoleMapper;
import com.example.inspection.service.evaluation.InspectionExecutionRoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspectionExecutionRoleServiceImpl implements InspectionExecutionRoleService {

    private final InspectionExecutionRoleRepository repository;
    private final InspectionExecutionRoleMapper mapper;

    @Override
    public InspectionExecutionRoleResponse create(InspectionExecutionRoleRequest request) {
        InspectionExecutionRole entity = mapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public InspectionExecutionRoleResponse getById(Long id) {
        InspectionExecutionRole entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InspectionExecutionRole not found with id " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<InspectionExecutionRoleResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<InspectionExecutionRoleResponse> getAllForPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).map(mapper::toResponse);
    }

    @Override
    public InspectionExecutionRoleResponse update(Long id, InspectionExecutionRoleRequest request) {
        InspectionExecutionRole entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InspectionExecutionRole not found with id " + id));
        mapper.updateEntity(entity, request);
        entity.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("InspectionExecutionRole not found with id " + id);
        }
        repository.deleteById(id);
    }

    
}
