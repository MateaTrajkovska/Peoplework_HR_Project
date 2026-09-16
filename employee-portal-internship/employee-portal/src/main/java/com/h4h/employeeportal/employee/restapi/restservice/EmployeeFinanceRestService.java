package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.EmployeeFinance;
import com.h4h.employeeportal.employee.core.service.EmployeeFinanceService;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.mapper.EmployeeFinanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeFinanceRestService {

    private final EmployeeFinanceService employeeFinanceService;
    private final EmployeeFinanceMapper employeeFinanceMapper;

    public EmployeeFinanceDto createEmployeeFinance(CreateEmployeeFinanceDto createEmployeeFinanceDto) {
        return employeeFinanceMapper.toDto(employeeFinanceService.createEmployeeFinance(
                employeeFinanceMapper.toEntityCreate(createEmployeeFinanceDto)));
    }

    public List<EmployeeFinanceDto> getAllEmployeeFinances() {
        return employeeFinanceMapper.toDtoList(employeeFinanceService.getAllEmployeeFinances());
    }

    public EmployeeFinanceDto getEmployeeFinanceById(UUID employeeFinanceUuid) {
        EmployeeFinance employeeFinance = employeeFinanceService.getEmployeeFinanceById(employeeFinanceUuid);
        return employeeFinanceMapper.toDto(employeeFinance);
    }

    public EmployeeFinanceDto updateEmployeeFinance(
            UUID employeeFinanceUuid, UpdateEmployeeFinanceDto updateEmployeeFinanceDto) {
        EmployeeFinance updatedEmployeeFinance = employeeFinanceService.updateEmployeeFinance(
                employeeFinanceUuid, employeeFinanceMapper.toEntityUpdate(updateEmployeeFinanceDto));
        return employeeFinanceMapper.toDto(updatedEmployeeFinance);
    }

    public DeleteDto deleteEmployeeFinance(UUID employeeFinanceUuid) {
        return employeeFinanceMapper.toDeletedDto(employeeFinanceService.deleteEmployeeFinance(employeeFinanceUuid));
    }
}
