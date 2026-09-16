package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeFinanceRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("core/employee/finance")
@CrossOrigin("*")
public class EmployeeFinanceRestController {

    private final EmployeeFinanceRestService employeeFinanceRestService;

    @PostMapping
    public EmployeeFinanceDto createEmployeeFinance(
            @Valid @RequestBody CreateEmployeeFinanceDto createEmployeeFinanceDto) {
        return employeeFinanceRestService.createEmployeeFinance(createEmployeeFinanceDto);
    }

    @GetMapping
    public List<EmployeeFinanceDto> getAllEmployeeFinances() {
        return employeeFinanceRestService.getAllEmployeeFinances();
    }

    @GetMapping("/{id}")
    public EmployeeFinanceDto getEmployeeFinanceById(@PathVariable UUID id) {
        return employeeFinanceRestService.getEmployeeFinanceById(id);
    }

    @PutMapping("/{id}")
    public EmployeeFinanceDto updateEmployeeFinance(
            @PathVariable UUID id, @RequestBody UpdateEmployeeFinanceDto updateEmployeeFinanceDto) {
        return employeeFinanceRestService.updateEmployeeFinance(id, updateEmployeeFinanceDto);
    }

    @DeleteMapping("/{id}")
    public DeleteDto deleteEmployeeFinance(@PathVariable UUID id) {
        return employeeFinanceRestService.deleteEmployeeFinance(id);
    }
}
