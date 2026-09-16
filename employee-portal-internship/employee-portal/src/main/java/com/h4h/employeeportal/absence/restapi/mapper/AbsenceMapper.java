package com.h4h.employeeportal.absence.restapi.mapper;

import com.h4h.employeeportal.absence.core.model.Absence;
import com.h4h.employeeportal.absence.restapi.dto.AbsenceDto;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.absence.restapi.dto.update.UpdateAbsenceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AbsenceMapper {

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "employeeId", source = "employeeUuid")
    AbsenceDto toDto(Absence entity);
    DeleteDto toDeletedDto(UUID uuid);

    List<AbsenceDto> toDtoList(List<Absence> entities);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "maxDays", ignore = true)
    @Mapping(target = "usedDays", ignore = true)
    @Mapping(target = "validFrom", ignore = true)
    @Mapping(target = "validTo", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    Absence toEntityUpdate(UpdateAbsenceDto dto);
}