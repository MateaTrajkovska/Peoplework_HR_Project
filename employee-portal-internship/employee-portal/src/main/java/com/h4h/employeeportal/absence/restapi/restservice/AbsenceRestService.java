package com.h4h.employeeportal.absence.restapi.restservice;

import com.h4h.employeeportal.absence.core.model.Absence;
import com.h4h.employeeportal.absence.core.service.AbsenceService;
import com.h4h.employeeportal.absence.restapi.dto.AbsenceDto;
import com.h4h.employeeportal.absence.restapi.dto.create.CreateAbsenceDto;
import com.h4h.employeeportal.absence.restapi.dto.update.UpdateAbsenceDto;
import com.h4h.employeeportal.absence.restapi.mapper.AbsenceMapper;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AbsenceRestService {

    private final AbsenceService absenceService;
    private final AbsenceMapper absenceMapper;

    @Transactional
    public AbsenceDto createAbsence(CreateAbsenceDto createAbsenceDto) {
        Absence savedAbsence = absenceService.createAbsence(
                createAbsenceDto.getEmployeeId(),
                createAbsenceDto.getYear()
        );
        return absenceMapper.toDto(savedAbsence);
    }

    public List<AbsenceDto> getAllAbsences() {
        return absenceMapper.toDtoList(absenceService.getAllAbsences());
    }

    public AbsenceDto getAbsenceById(UUID absenceUuid) {
        return absenceMapper.toDto(
                absenceService.getActiveAbsenceById(absenceUuid)
        );
    }

    @Transactional
    public AbsenceDto updateAbsenceStatus(UUID absenceUuid, UpdateAbsenceDto updateAbsenceDto) {
        Absence updated = absenceService.updateAbsenceStatus(
                absenceUuid,
                absenceMapper.toEntityUpdate(updateAbsenceDto)
        );
        return absenceMapper.toDto(updated);
    }

    public DeleteDto deleteAbsence(UUID absenceUuid) {
        return absenceMapper.toDeletedDto(absenceService.deleteAbsence(absenceUuid));
    }
}
