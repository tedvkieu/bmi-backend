package com.example.inspection.implementation.evaluation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.evaluation.DossierDocumentsChecklistRequest;
import com.example.inspection.dto.response.evaluation.DossierDocumentsChecklistResponse;
import com.example.inspection.entity.evaluation.DossierDocumentsChecklist;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.repository.evaluation.DossierDocumentsChecklistRepository;
import com.example.inspection.mapper.evaluation.DossierDocumentsChecklistMapper;
import com.example.inspection.service.evaluation.DossierDocumentsChecklistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DossierDocumentsChecklistServiceImpl implements DossierDocumentsChecklistService {

    private final DossierDocumentsChecklistRepository repository;
    private final DossierDocumentsChecklistMapper mapper;

    @Override
    public DossierDocumentsChecklistResponse create(DossierDocumentsChecklistRequest request) {
        DossierDocumentsChecklist entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DossierDocumentsChecklistResponse getById(Long id) {
        DossierDocumentsChecklist entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DossierDocumentsChecklist not found with id " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<DossierDocumentsChecklistResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<DossierDocumentsChecklistResponse> getAllForPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).map(mapper::toResponse);
    }

    @Override
    public DossierDocumentsChecklistResponse update(Long id, DossierDocumentsChecklistRequest request) {
        DossierDocumentsChecklist entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DossierDocumentsChecklist not found with id " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DossierDocumentsChecklist not found with id " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<DossierDocumentsChecklistResponse> getByEvaluationId(Long evaluationId) {
        return repository.findByEvaluationId(evaluationId).stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    
}
