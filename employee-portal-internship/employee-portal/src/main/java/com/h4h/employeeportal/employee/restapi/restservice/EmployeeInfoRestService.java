package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.EmployeeInfo;
import com.h4h.employeeportal.employee.core.service.EmployeeInfoService;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.mapper.EmployeeInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeInfoRestService {

    private final EmployeeInfoService employeeInfoService;
    private final EmployeeInfoMapper employeeInfoMapper;

    public EmployeeInfoDto createEmployeeInfo(CreateEmployeeInfoDto createEmployeeInfoDto) {
        EmployeeInfo employeeInfo =
                employeeInfoService.createEmployeeInfo(employeeInfoMapper.toEntityCreate(createEmployeeInfoDto));
        return employeeInfoMapper.toDto(employeeInfo);
    }

    public List<EmployeeInfoDto> getAllEmployeeInfo() {
        return employeeInfoMapper.toDtoList(employeeInfoService.getAllEmployeeInfo());
    }

    public EmployeeInfoDto getEmployeeInfoById(UUID infoUuid) {
        return employeeInfoMapper.toDto(employeeInfoService.getEmployeeInfoById(infoUuid));
    }

    public EmployeeInfoDto updateEmployeeInfo(UUID infoUuid, UpdateEmployeeInfoDto updateEmployeeInfoDto) {
        EmployeeInfo updatedEmployeeInfo = employeeInfoService.updateEmployeeInfo(
                infoUuid, employeeInfoMapper.toEntityUpdate(updateEmployeeInfoDto));
        return employeeInfoMapper.toDto(updatedEmployeeInfo);
    }

    public DeleteDto deleteEmployeeInfo(UUID infoUuid) {

        return employeeInfoMapper.toDeletedDto(employeeInfoService.deleteEmployeeInfoById(infoUuid));
    }
}
