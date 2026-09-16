package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeDetailsDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeListDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("core/employee")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeRestController {

    private final EmployeeRestService employeeRestService;

    @PostMapping
    public EmployeeDto createEmployee(@Valid @RequestBody CreateEmployeeDto createEmployeeDto) {
        return employeeRestService.createEmployee(createEmployeeDto);
    }

    @GetMapping
    public List<EmployeeListDto> getAllEmployees() {
        return employeeRestService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeDetailsDto getEmployeeById(@PathVariable UUID id) {
        return employeeRestService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public EmployeeDto updateEmployee(@PathVariable UUID id, @RequestBody UpdateEmployeeDto updateEmployeeDto) {
        return employeeRestService.updateEmployee(id, updateEmployeeDto);
    }

    @DeleteMapping("/{id}")
    public DeleteDto deleteEmployee(@PathVariable UUID id) {
        return employeeRestService.deleteEmployee(id);
    }
}
