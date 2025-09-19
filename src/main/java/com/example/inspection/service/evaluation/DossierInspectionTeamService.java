package com.example.inspection.service.evaluation;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.evaluation.DossierInspectionTeamRequest;
import com.example.inspection.dto.response.evaluation.DossierInspectionTeamResponse;

public interface DossierInspectionTeamService {
    DossierInspectionTeamResponse create(DossierInspectionTeamRequest request);
    DossierInspectionTeamResponse getById(Long id);
    List<DossierInspectionTeamResponse> getAll();
    Page<DossierInspectionTeamResponse> getAllForPage(int page, int size);
    DossierInspectionTeamResponse update(Long id, DossierInspectionTeamRequest request);
    void delete(Long id);

    List<DossierInspectionTeamResponse> getByDossierId(Long dossierId);
}

