package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.model.EmployeeReport;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EmployeeReportMapper {

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "employeeId", source = "employeeUuid")
    EmployeeReportDto toDto(EmployeeReport entity);

    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "employeeUuid", source = "employeeId")
    EmployeeReport toEntity(EmployeeReportDto dto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", source = "employeeId")
    @Mapping(target = "reportStatus", ignore = true)
    EmployeeReport toEntityCreate(CreateEmployeeReportDto createEmployeeReportDto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    @Mapping(target = "reportStatus", ignore = true)
    EmployeeReport toEntityCreateFromEmployee(CreateEmployeeDto createEmployeeDto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    EmployeeReport toEntityUpdate(UpdateEmployeeReportDto updateEmployeeReportDto);

    DeleteDto toDeletedDto(UUID uuid);

    List<EmployeeReportDto> toDtoList(List<EmployeeReport> entities);
}
