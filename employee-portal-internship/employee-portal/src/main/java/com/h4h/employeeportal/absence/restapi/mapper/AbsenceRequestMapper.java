package com.h4h.employeeportal.absence.restapi.mapper;

import com.h4h.employeeportal.absence.core.model.AbsenceRequest;
import com.h4h.employeeportal.absence.restapi.dto.AbsenceRequestDto;
import com.h4h.employeeportal.absence.restapi.dto.create.CreateAbsenceRequestDto;
import com.h4h.employeeportal.absence.restapi.dto.update.UpdateAbsenceRequestDto;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AbsenceRequestMapper {

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "absenceId", source = "absenceUuid")
    @Mapping(
            target = "daysUsed",
            expression = "java(calculateDaysUsed(entity))"
    )
    AbsenceRequestDto toDto(AbsenceRequest entity);
    DeleteDto toDeletedDto(UUID uuid);

    List<AbsenceRequestDto> toDtoList(List<AbsenceRequest> entities);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "decisionNumber", ignore = true)
    @Mapping(target = "daysUsed", ignore = true)
    @Mapping(target = "absenceRequestStatus", ignore = true)
    @Mapping(target = "daysUsedFromPreviousYear", ignore = true)
    @Mapping(target = "daysUsedFromCurrentYear", ignore = true)
    @Mapping(target = "absenceUuid", source = "absenceId")
    AbsenceRequest toEntityCreate(CreateAbsenceRequestDto dto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "decisionNumber", ignore = true)
    @Mapping(target = "decisionDate", ignore = true)
    @Mapping(target = "daysUsed", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "absenceRequestStatus", ignore = true)
    @Mapping(target = "daysUsedFromPreviousYear", ignore = true)
    @Mapping(target = "daysUsedFromCurrentYear", ignore = true)
    @Mapping(target = "absenceUuid", ignore = true)
    AbsenceRequest toEntityUpdate(UpdateAbsenceRequestDto dto);

    default int calculateDaysUsed(AbsenceRequest entity) {
        int daysFromPreviousYear =
                entity.getDaysUsedFromPreviousYear() == null
                        ? 0
                        : entity.getDaysUsedFromPreviousYear();

        int daysFromCurrentYear =
                entity.getDaysUsedFromCurrentYear() == null
                        ? 0
                        : entity.getDaysUsedFromCurrentYear();

        return daysFromPreviousYear + daysFromCurrentYear;
    }
}