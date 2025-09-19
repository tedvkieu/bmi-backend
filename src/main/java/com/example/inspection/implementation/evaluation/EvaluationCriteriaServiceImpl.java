package com.example.inspection.implementation.evaluation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.evaluation.EvaluationCriteriaRequest;
import com.example.inspection.dto.response.evaluation.EvaluationCriteriaResponse;
import com.example.inspection.entity.evaluation.EvaluationCriteria;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.repository.evaluation.EvaluationCriteriaRepository;
import com.example.inspection.mapper.evaluation.EvaluationCriteriaMapper;
import com.example.inspection.service.evaluation.EvaluationCriteriaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {

    private final EvaluationCriteriaRepository repository;
    private final EvaluationCriteriaMapper mapper;

    @Override
    public EvaluationCriteriaResponse create(EvaluationCriteriaRequest request) {
        EvaluationCriteria entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public EvaluationCriteriaResponse getById(Long id) {
        EvaluationCriteria entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EvaluationCriteria not found with id " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<EvaluationCriteriaResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<EvaluationCriteriaResponse> getAllForPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).map(mapper::toResponse);
    }

    @Override
    public EvaluationCriteriaResponse update(Long id, EvaluationCriteriaRequest request) {
        EvaluationCriteria entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EvaluationCriteria not found with id " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("EvaluationCriteria not found with id " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<EvaluationCriteriaResponse> getByCategoryId(Long categoryId) {
        return repository.findByCategoryId(categoryId).stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    
}
