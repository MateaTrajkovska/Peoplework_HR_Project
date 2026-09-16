package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.EmployeeReport;
import com.h4h.employeeportal.employee.core.service.EmployeeReportService;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.mapper.EmployeeReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeReportRestService {

    private final EmployeeReportService employeeReportService;
    private final EmployeeReportMapper employeeReportMapper;

    public EmployeeReportDto createEmployeeReport(CreateEmployeeReportDto createEmployeeReportDto) {
        EmployeeReport employeeReport = employeeReportService.createEmployeeReport(
                employeeReportMapper.toEntityCreate(createEmployeeReportDto));
        return employeeReportMapper.toDto(employeeReport);
    }

    public List<EmployeeReportDto> getAllEmployeeReports() {
        return employeeReportMapper.toDtoList(employeeReportService.getAllEmployeeReports());
    }

    public EmployeeReportDto getEmployeeReportById(UUID reportUuid) {
        return employeeReportMapper.toDto(employeeReportService.getEmployeeReportById(reportUuid));
    }

    public EmployeeReportDto updateEmployeeReport(UUID reportUuid, UpdateEmployeeReportDto updateEmployeeReportDto) {
        EmployeeReport updatedEmployeereport = employeeReportService.updateEmployeeReport(
                reportUuid, employeeReportMapper.toEntityUpdate(updateEmployeeReportDto));
        return employeeReportMapper.toDto(updatedEmployeereport);
    }

    public DeleteDto deleteEmployeeReport(UUID reportUuid) {
        return employeeReportMapper.toDeletedDto(employeeReportService.deleteEmployeeReportById(reportUuid));
    }
}
