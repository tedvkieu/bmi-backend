package com.example.inspection.implementation.evaluation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.evaluation.DossierDocumentTypeRequest;
import com.example.inspection.dto.response.evaluation.DossierDocumentTypeResponse;
import com.example.inspection.entity.evaluation.DossierDocumentType;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.mapper.evaluation.DossierDocumentTypeMapper;
import com.example.inspection.repository.evaluation.DossierDocumentTypeRepository;
import com.example.inspection.service.evaluation.DossierDocumentTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DossierDocumentTypeServiceImpl implements DossierDocumentTypeService {

    private final DossierDocumentTypeRepository repository;
    private final DossierDocumentTypeMapper mapper;

    @Override
    public DossierDocumentTypeResponse create(DossierDocumentTypeRequest request) {
        DossierDocumentType entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DossierDocumentTypeResponse getById(Long id) {
        DossierDocumentType entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DossierDocumentType not found with id " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<DossierDocumentTypeResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<DossierDocumentTypeResponse> getAllForPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).map(mapper::toResponse);
    }

    @Override
    public DossierDocumentTypeResponse update(Long id, DossierDocumentTypeRequest request) {
        DossierDocumentType entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DossierDocumentType not found with id " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DossierDocumentType not found with id " + id);
        }
        repository.deleteById(id);
    }
}
