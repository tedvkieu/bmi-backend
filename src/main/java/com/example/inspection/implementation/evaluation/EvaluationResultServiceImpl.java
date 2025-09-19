package com.example.inspection.implementation.evaluation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.evaluation.EvaluationResultRequest;
import com.example.inspection.dto.response.evaluation.EvaluationResultResponse;
import com.example.inspection.entity.evaluation.EvaluationResult;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.repository.evaluation.EvaluationResultRepository;
import com.example.inspection.mapper.evaluation.EvaluationResultMapper;
import com.example.inspection.service.evaluation.EvaluationResultService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationResultServiceImpl implements EvaluationResultService {

    private final EvaluationResultRepository repository;
    private final EvaluationResultMapper mapper;

    @Override
    public EvaluationResultResponse create(EvaluationResultRequest request) {
        EvaluationResult entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public EvaluationResultResponse getById(Long id) {
        EvaluationResult entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EvaluationResult not found with id " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<EvaluationResultResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<EvaluationResultResponse> getAllForPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).map(mapper::toResponse);
    }

    @Override
    public EvaluationResultResponse update(Long id, EvaluationResultRequest request) {
        EvaluationResult entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EvaluationResult not found with id " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("EvaluationResult not found with id " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<EvaluationResultResponse> getByEvaluationId(Long evaluationId) {
        return repository.findByEvaluationId(evaluationId).stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    
}
