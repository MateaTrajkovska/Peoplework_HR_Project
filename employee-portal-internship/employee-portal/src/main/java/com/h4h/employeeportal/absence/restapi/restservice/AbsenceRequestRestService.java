package com.h4h.employeeportal.absence.restapi.restservice;

import com.h4h.employeeportal.absence.core.model.AbsenceRequest;
import com.h4h.employeeportal.absence.core.service.AbsenceRequestService;
import com.h4h.employeeportal.absence.restapi.dto.AbsenceRequestDto;
import com.h4h.employeeportal.absence.restapi.dto.create.CreateAbsenceRequestDto;
import com.h4h.employeeportal.absence.restapi.dto.update.UpdateAbsenceRequestDto;
import com.h4h.employeeportal.absence.restapi.mapper.AbsenceRequestMapper;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AbsenceRequestRestService {

    private final AbsenceRequestService absenceRequestService;
    private final AbsenceRequestMapper absenceRequestMapper;

    @Transactional
    public AbsenceRequestDto createAbsenceRequest(CreateAbsenceRequestDto createAbsenceRequestDto) {
        AbsenceRequest saved = absenceRequestService.createAbsenceRequest(
                absenceRequestMapper.toEntityCreate(createAbsenceRequestDto)
        );
        return absenceRequestMapper.toDto(saved);
    }

    public List<AbsenceRequestDto> getAllAbsenceRequests() {
        return absenceRequestMapper.toDtoList(absenceRequestService.getAllAbsenceRequests());
    }

    public AbsenceRequestDto getAbsenceRequestById(UUID absenceRequestUuid) {
        return absenceRequestMapper.toDto(absenceRequestService.getAbsenceRequestById(absenceRequestUuid));
    }

    @Transactional
    public AbsenceRequestDto updateAbsenceRequest(UUID absenceRequestUuid, UpdateAbsenceRequestDto updateAbsenceRequestDto) {
        AbsenceRequest updated = absenceRequestService.updateAbsenceRequest(
                absenceRequestUuid,
                absenceRequestMapper.toEntityUpdate(updateAbsenceRequestDto)
        );
        return absenceRequestMapper.toDto(updated);
    }

    public DeleteDto deleteAbsenceRequest(UUID absenceRequestUuid) {
        return absenceRequestMapper.toDeletedDto(absenceRequestService.deleteAbsenceRequest(absenceRequestUuid));
    }

    @Transactional
    public AbsenceRequestDto approveAbsenceRequest(UUID absenceRequestUuid) {
        AbsenceRequest approved = absenceRequestService.approveAbsenceRequest(absenceRequestUuid);
        return absenceRequestMapper.toDto(approved);
    }

    @Transactional
    public AbsenceRequestDto denyAbsenceRequest(UUID absenceRequestUuid) {
        AbsenceRequest denied = absenceRequestService.denyAbsenceRequest(absenceRequestUuid);

        return absenceRequestMapper.toDto(denied);
    }
}