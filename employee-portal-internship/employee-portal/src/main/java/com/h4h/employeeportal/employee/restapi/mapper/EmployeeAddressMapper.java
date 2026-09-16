package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.model.EmployeeAddress;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeAddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EmployeeAddressMapper {

    @Mapping(source = "uuid", target = "id")
    @Mapping(source = "employeeUuid", target = "employeeId")
    EmployeeAddressDto toDto(EmployeeAddress entity);

    @Mapping(source = "id", target = "uuid")
    @Mapping(source = "employeeId", target = "employeeUuid")
    EmployeeAddress toEntity(EmployeeAddressDto dto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", source = "employeeId")
    @Mapping(target = "addressStatus", ignore = true)
    EmployeeAddress toEntityCreate(CreateEmployeeAddressDto createEmployeeAddressDto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    @Mapping(target = "addressStatus", ignore = true)
    EmployeeAddress toEntityCreateFromEmployee(CreateEmployeeDto createEmployeeDto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    EmployeeAddress toEntityUpdate(UpdateEmployeeAddressDto updateEmployeeAddressDto);

    DeleteDto toDeletedDto(UUID uuid);

    List<EmployeeAddressDto> toDtoList(List<EmployeeAddress> entities);
}
