package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.model.EmployeeContact;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeContactDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EmployeeContactMapper {

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "employeeId", source = "employeeUuid")
    EmployeeContactDto toDto(EmployeeContact entity);

    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "employeeUuid", source = "employeeId")
    EmployeeContact toEntity(EmployeeContactDto dto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    @Mapping(target = "contactStatus", ignore = true)
    EmployeeContact toEntityCreate(CreateEmployeeContactDto createEmployeeContactDto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    @Mapping(target = "contactStatus", ignore = true)
    EmployeeContact toEntityCreateFromEmployee(CreateEmployeeDto createEmployeeDto);

    @Mapping(target = "employeeUuid", ignore = true)
    EmployeeContact toEntityUpdate(UpdateEmployeeContactDto updateEmployeeContactDto);

    DeleteDto toDeletedDto(UUID uuid);

    List<EmployeeContactDto> toDtoList(List<EmployeeContact> entity);
}
