package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeReportRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/core/employee/report")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeReportRestController {

    private final EmployeeReportRestService employeeReportRestService;

    @PostMapping
    public EmployeeReportDto createEmployeeReport(@Valid @RequestBody CreateEmployeeReportDto createEmployeeReportDto) {
        return employeeReportRestService.createEmployeeReport(createEmployeeReportDto);
    }

    @GetMapping
    public List<EmployeeReportDto> getAllEmployeeReports() {
        return employeeReportRestService.getAllEmployeeReports();
    }

    @GetMapping("/{id}")
    public EmployeeReportDto getEmployeeReportById(@PathVariable UUID id) {
        return employeeReportRestService.getEmployeeReportById(id);
    }

    @PutMapping("/{id}")
    public EmployeeReportDto updateEmployeeReport(
            @PathVariable UUID id, @RequestBody UpdateEmployeeReportDto updateEmployeeReportDto) {
        return employeeReportRestService.updateEmployeeReport(id, updateEmployeeReportDto);
    }

    @DeleteMapping("/{id}")
    public DeleteDto deleteEmployeeReportById(@PathVariable UUID id) {
        return employeeReportRestService.deleteEmployeeReport(id);
    }
}
