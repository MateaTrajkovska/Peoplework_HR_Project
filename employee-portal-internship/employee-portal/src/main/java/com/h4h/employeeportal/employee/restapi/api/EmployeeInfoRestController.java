package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeInfoRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/core/employee/info")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeInfoRestController {

    private final EmployeeInfoRestService employeeInfoRestService;

    @PostMapping
    public EmployeeInfoDto createEmployeeInfo(@Valid @RequestBody CreateEmployeeInfoDto createEmployeeInfoDto) {
        return employeeInfoRestService.createEmployeeInfo(createEmployeeInfoDto);
    }

    @GetMapping
    public List<EmployeeInfoDto> getAllEmployeeInfo() {
        return employeeInfoRestService.getAllEmployeeInfo();
    }

    @GetMapping("/{id}")
    public EmployeeInfoDto getEmployeeInfoById(@PathVariable UUID id) {
        return employeeInfoRestService.getEmployeeInfoById(id);
    }

    @PutMapping("/{id}")
    public EmployeeInfoDto updateEmployeeInfo(
            @PathVariable UUID id, @RequestBody UpdateEmployeeInfoDto updateEmployeeInfoDto) {
        return employeeInfoRestService.updateEmployeeInfo(id, updateEmployeeInfoDto);
    }

    @DeleteMapping("/{id}")
    public DeleteDto deleteEmployeeInfoById(@PathVariable UUID id) {
        return employeeInfoRestService.deleteEmployeeInfo(id);
    }
}
