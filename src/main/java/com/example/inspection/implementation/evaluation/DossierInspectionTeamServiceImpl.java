package com.example.inspection.implementation.evaluation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.inspection.dto.request.evaluation.DossierInspectionTeamRequest;
import com.example.inspection.dto.response.evaluation.DossierInspectionTeamResponse;
import com.example.inspection.entity.evaluation.DossierInspectionTeam;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.repository.evaluation.DossierInspectionTeamRepository;
import com.example.inspection.mapper.evaluation.DossierInspectionTeamMapper;
import com.example.inspection.service.evaluation.DossierInspectionTeamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DossierInspectionTeamServiceImpl implements DossierInspectionTeamService {

    private final DossierInspectionTeamRepository repository;
    private final DossierInspectionTeamMapper mapper;

    @Override
    public DossierInspectionTeamResponse create(DossierInspectionTeamRequest request) {
        DossierInspectionTeam entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DossierInspectionTeamResponse getById(Long id) {
        DossierInspectionTeam entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DossierInspectionTeam not found with id " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<DossierInspectionTeamResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<DossierInspectionTeamResponse> getAllForPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).map(mapper::toResponse);
    }

    @Override
    public DossierInspectionTeamResponse update(Long id, DossierInspectionTeamRequest request) {
        DossierInspectionTeam entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DossierInspectionTeam not found with id " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DossierInspectionTeam not found with id " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<DossierInspectionTeamResponse> getByDossierId(Long dossierId) {
        return repository.findByDossierId(dossierId).stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    
}
