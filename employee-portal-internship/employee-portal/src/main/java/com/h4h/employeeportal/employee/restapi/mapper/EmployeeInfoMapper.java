package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.model.EmployeeInfo;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EmployeeInfoMapper {

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "employeeId", source = "employeeUuid")
    EmployeeInfoDto toDto(EmployeeInfo entity);

    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "employeeUuid", source = "employeeId")
    EmployeeInfo toEntity(EmployeeInfoDto dto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", source = "employeeId")
    @Mapping(target = "infoStatus", ignore = true)
    EmployeeInfo toEntityCreate(CreateEmployeeInfoDto createEmployeeInfoDto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    @Mapping(target = "infoStatus", ignore = true)
    @Mapping(target = "degree", source = "degree")
    EmployeeInfo toEntityCreateFromEmployee(CreateEmployeeDto createEmployeeDto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "employeeUuid", ignore = true)
    EmployeeInfo toEntityUpdate(UpdateEmployeeInfoDto updateEmployeeInfoDto);

    DeleteDto toDeletedDto(UUID uuid);

    List<EmployeeInfoDto> toDtoList(List<EmployeeInfo> entities);
}
