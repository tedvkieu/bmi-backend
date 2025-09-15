// package com.example.inspection.implementation;

// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import com.example.inspection.dto.request.ExecutionUnitRequest;
// import com.example.inspection.dto.request.ExecutionUnitResponse;
// import com.example.inspection.entity.User;
// import com.example.inspection.entity.Dossier;
// import com.example.inspection.entity.ExecutionUnit;
// import com.example.inspection.exception.ResourceNotFoundException;
// import com.example.inspection.mapper.ExecutionUnitMapper;
// import com.example.inspection.repository.UserRepository;
// import com.example.inspection.repository.DossierRepository;
// import com.example.inspection.repository.ExecutionUnitRepository;
// import com.example.inspection.service.ExecutionUnitService;

// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// public class ExecutionUnitServiceImpl implements ExecutionUnitService {

// private final ExecutionUnitRepository executionUnitRepository;
// private final DossierRepository dossierRepository;
// private final UserRepository userRepository;
// private final ExecutionUnitMapper executionUnitMapper;

// @Override
// public ExecutionUnitResponse create(ExecutionUnitRequest request) {
// Dossier dossier = dossierRepository.findById(request.getDossierId())
// .orElseThrow(
// () -> new ResourceNotFoundException("Dossier not found with id " +
// request.getDossierId()));
// User user = userRepository.findById(request.getUserId())
// .orElseThrow(
// () -> new ResourceNotFoundException("User not found with id " +
// request.getUserId()));

// ExecutionUnit executionUnit = executionUnitMapper.toEntity(request, dossier,
// user);
// return
// executionUnitMapper.toResponse(executionUnitRepository.save(executionUnit));
// }

// @Override
// public ExecutionUnitResponse getById(Long id) {
// ExecutionUnit executionUnit = executionUnitRepository.findById(id)
// .orElseThrow(() -> new ResourceNotFoundException("ExecutionUnit not found
// with id " + id));
// return executionUnitMapper.toResponse(executionUnit);
// }

// @Override
// public List<ExecutionUnitResponse> getAll() {
// return executionUnitRepository.findAll()
// .stream()
// .map(executionUnitMapper::toResponse)
// .collect(Collectors.toList());
// }

// @Override
// public ExecutionUnitResponse update(Long id, ExecutionUnitRequest request) {
// ExecutionUnit executionUnit = executionUnitRepository.findById(id)
// .orElseThrow(() -> new ResourceNotFoundException("ExecutionUnit not found
// with id " + id));

// Dossier dossier = dossierRepository.findById(request.getDossierId())
// .orElseThrow(
// () -> new ResourceNotFoundException("Dossier not found with id " +
// request.getDossierId()));
// User user = userRepository.findById(request.getUserId())
// .orElseThrow(
// () -> new ResourceNotFoundException("User not found with id " +
// request.getUserId()));

// executionUnitMapper.updateEntity(executionUnit, request, dossier, user);
// return
// executionUnitMapper.toResponse(executionUnitRepository.save(executionUnit));
// }

// @Override
// public void delete(Long id) {
// if (!executionUnitRepository.existsById(id)) {
// throw new ResourceNotFoundException("ExecutionUnit not found with id " + id);
// }
// executionUnitRepository.deleteById(id);
// }
// }
