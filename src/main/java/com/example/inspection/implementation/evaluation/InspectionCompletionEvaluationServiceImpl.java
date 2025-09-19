package com.example.inspection.implementation.evaluation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.evaluation.InspectionCompletionEvaluationRequest;
import com.example.inspection.dto.response.evaluation.InspectionCompletionEvaluationResponse;
import com.example.inspection.entity.evaluation.InspectionCompletionEvaluation;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.repository.evaluation.InspectionCompletionEvaluationRepository;
import com.example.inspection.mapper.evaluation.InspectionCompletionEvaluationMapper;
import com.example.inspection.service.evaluation.InspectionCompletionEvaluationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspectionCompletionEvaluationServiceImpl implements InspectionCompletionEvaluationService {

    private final InspectionCompletionEvaluationRepository repository;
    private final InspectionCompletionEvaluationMapper mapper;

    @Override
    public InspectionCompletionEvaluationResponse create(InspectionCompletionEvaluationRequest request) {
        InspectionCompletionEvaluation entity = mapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public InspectionCompletionEvaluationResponse getById(Long id) {
        InspectionCompletionEvaluation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InspectionCompletionEvaluation not found with id " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<InspectionCompletionEvaluationResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<InspectionCompletionEvaluationResponse> getAllForPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).map(mapper::toResponse);
    }

    @Override
    public InspectionCompletionEvaluationResponse update(Long id, InspectionCompletionEvaluationRequest request) {
        InspectionCompletionEvaluation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InspectionCompletionEvaluation not found with id " + id));
        mapper.updateEntity(entity, request);
        entity.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("InspectionCompletionEvaluation not found with id " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<InspectionCompletionEvaluationResponse> getByDossierId(Long dossierId) {
        return repository.findByDossierId(dossierId).stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    
}
