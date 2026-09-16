package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.EmployeeAddress;
import com.h4h.employeeportal.employee.core.service.EmployeeAddressService;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.mapper.EmployeeAddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeAddressRestService {

    private final EmployeeAddressService employeeAddressService;
    private final EmployeeAddressMapper employeeAddressMapper;

    public EmployeeAddressDto createEmployeeAddress(CreateEmployeeAddressDto createEmployeeAddressDto) {

        EmployeeAddress employeeAddress = employeeAddressService.createEmployeeAddress(
                employeeAddressMapper.toEntityCreate(createEmployeeAddressDto));
        return employeeAddressMapper.toDto(employeeAddress);
    }

    public List<EmployeeAddressDto> getAllEmployeeAddresses() {

        return employeeAddressMapper.toDtoList(employeeAddressService.getAllEmployeeAddresses());
    }

    public EmployeeAddressDto getEmployeeAddressById(UUID addressUuid) {
        return employeeAddressMapper.toDto(employeeAddressService.getEmployeeAddressById(addressUuid));
    }

    public EmployeeAddressDto updateEmployeeAddress(
            UUID addressUuid, UpdateEmployeeAddressDto updateEmployeeAddressDto) {
        EmployeeAddress updatedEmployeeAddress = employeeAddressService.updateEmployeeAddress(
                addressUuid, employeeAddressMapper.toEntityUpdate(updateEmployeeAddressDto));
        return employeeAddressMapper.toDto(updatedEmployeeAddress);
    }

    public DeleteDto deleteEmployeeAddress(UUID addressUuid) {
        return employeeAddressMapper.toDeletedDto(employeeAddressService.deleteEmployeeAddress(addressUuid));
    }
}
