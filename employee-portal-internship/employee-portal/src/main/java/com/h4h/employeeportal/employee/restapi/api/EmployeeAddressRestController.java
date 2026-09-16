package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeAddressRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/core/employee/address")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeAddressRestController {

    private final EmployeeAddressRestService employeeAddressRestService;

    @PostMapping
    public EmployeeAddressDto createEmployeeAddress(
            @Valid @RequestBody CreateEmployeeAddressDto createEmployeeAddressDto) {
        return employeeAddressRestService.createEmployeeAddress(createEmployeeAddressDto);
    }

    @GetMapping
    public List<EmployeeAddressDto> getAllEmployeeAddresses() {
        return employeeAddressRestService.getAllEmployeeAddresses();
    }

    @GetMapping("/{id}")
    public EmployeeAddressDto getEmployeeAddressById(@PathVariable UUID id) {
        return employeeAddressRestService.getEmployeeAddressById(id);
    }

    @PutMapping("/{id}")
    public EmployeeAddressDto updateEmployeeAddress(
            @PathVariable UUID id, @RequestBody UpdateEmployeeAddressDto updateEmployeeAddressDto) {
        return employeeAddressRestService.updateEmployeeAddress(id, updateEmployeeAddressDto);
    }

    @DeleteMapping("/{id}")
    public DeleteDto deleteEmployeeAddress(@PathVariable UUID id) {
        return employeeAddressRestService.deleteEmployeeAddress(id);
    }
}
