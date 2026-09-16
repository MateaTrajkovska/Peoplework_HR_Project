package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.model.Employee;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeDetailsDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeListDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "userId", source = "userUuid")
    EmployeeDto toDto(Employee entity);

    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "userUuid", source = "userId")
    Employee toEntity(EmployeeDto dto);

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "userId", source = "userUuid")
    @Mapping(target = "employeeContacts", ignore = true)
    @Mapping(target = "employeeAddress", ignore = true)
    @Mapping(target = "employeeFinance", ignore = true)
    @Mapping(target = "employeeInfo", ignore = true)
    @Mapping(target = "employeeReport", ignore = true)
    EmployeeDetailsDto toDtoDetails(Employee entity);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "userUuid", source = "userId")
    Employee toEntityCreate(CreateEmployeeDto createEmployeeDto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeNumber", ignore = true)
    @Mapping(target = "userUuid", source = "userId")
    Employee toEntityUpdate(UpdateEmployeeDto updateEmployeeDto);

    DeleteDto toDeletedDto(UUID uuid);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "municipality", ignore = true)
    @Mapping(target = "id", source = "uuid")
    EmployeeListDto toListDto(Employee entity);

    List<EmployeeListDto> toDtoList(List<Employee> entity);
}
