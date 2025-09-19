package com.example.inspection.implementation.evaluation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.evaluation.EvaluationSignatureRequest;
import com.example.inspection.dto.response.evaluation.EvaluationSignatureResponse;
import com.example.inspection.entity.evaluation.EvaluationSignature;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.repository.evaluation.EvaluationSignatureRepository;
import com.example.inspection.mapper.evaluation.EvaluationSignatureMapper;
import com.example.inspection.service.evaluation.EvaluationSignatureService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationSignatureServiceImpl implements EvaluationSignatureService {

    private final EvaluationSignatureRepository repository;
    private final EvaluationSignatureMapper mapper;

    @Override
    public EvaluationSignatureResponse create(EvaluationSignatureRequest request) {
        EvaluationSignature entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public EvaluationSignatureResponse getById(Long id) {
        EvaluationSignature entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EvaluationSignature not found with id " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<EvaluationSignatureResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<EvaluationSignatureResponse> getAllForPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).map(mapper::toResponse);
    }

    @Override
    public EvaluationSignatureResponse update(Long id, EvaluationSignatureRequest request) {
        EvaluationSignature entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EvaluationSignature not found with id " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("EvaluationSignature not found with id " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<EvaluationSignatureResponse> getByEvaluationId(Long evaluationId) {
        return repository.findByEvaluationId(evaluationId).stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    
}
