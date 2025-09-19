package com.example.inspection.implementation.evaluation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.evaluation.EvaluationCategoryRequest;
import com.example.inspection.dto.response.evaluation.EvaluationCategoryResponse;
import com.example.inspection.entity.evaluation.EvaluationCategory;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.repository.evaluation.EvaluationCategoryRepository;
import com.example.inspection.mapper.evaluation.EvaluationCategoryMapper;
import com.example.inspection.service.evaluation.EvaluationCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationCategoryServiceImpl implements EvaluationCategoryService {

    private final EvaluationCategoryRepository repository;
    private final EvaluationCategoryMapper mapper;

    @Override
    public EvaluationCategoryResponse create(EvaluationCategoryRequest request) {
        EvaluationCategory entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public EvaluationCategoryResponse getById(Long id) {
        EvaluationCategory entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EvaluationCategory not found with id " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<EvaluationCategoryResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<EvaluationCategoryResponse> getAllForPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).map(mapper::toResponse);
    }

    @Override
    public EvaluationCategoryResponse update(Long id, EvaluationCategoryRequest request) {
        EvaluationCategory entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EvaluationCategory not found with id " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("EvaluationCategory not found with id " + id);
        }
        repository.deleteById(id);
    }

    
}
