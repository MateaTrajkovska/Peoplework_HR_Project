package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.model.EmployeeFinance;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeFinanceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EmployeeFinanceMapper {

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "employeeId", source = "employeeUuid")
    EmployeeFinanceDto toDto(EmployeeFinance entity);

    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "employeeUuid", source = "employeeId")
    EmployeeFinance toEntity(EmployeeFinanceDto dto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", source = "employeeId")
    @Mapping(target = "bankStatus", ignore = true)
    EmployeeFinance toEntityCreate(CreateEmployeeFinanceDto createEmployeeFinanceDto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    @Mapping(target = "bankStatus", ignore = true)
    EmployeeFinance toEntityCreateFromEmployee(CreateEmployeeDto createEmployeeDto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    EmployeeFinance toEntityUpdate(UpdateEmployeeFinanceDto updateEmployeeFinanceDto);

    DeleteDto toDeletedDto(UUID uuid);

    List<EmployeeFinanceDto> toDtoList(List<EmployeeFinance> entity);
}
