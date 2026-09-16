package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeContactRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("core/employee/contact")
@CrossOrigin("*")
public class EmployeeContactRestController {

    private final EmployeeContactRestService employeeContactRestService;

    @PostMapping
    public EmployeeContactDto createEmployeeContact(
            @Valid @RequestBody CreateEmployeeContactDto createEmployeeContactDto) {
        return employeeContactRestService.createEmployeeContact(createEmployeeContactDto);
    }

    @GetMapping
    public List<EmployeeContactDto> getAllEmployeeContacts() {
        return employeeContactRestService.getAllEmployeeContacts();
    }

    @GetMapping("/{id}")
    public EmployeeContactDto getEmployeeContactById(@PathVariable UUID id) {
        return employeeContactRestService.getEmployeeContactById(id);
    }

    @PutMapping("/{id}")
    public EmployeeContactDto updateEmployeeContact(
            @PathVariable UUID id, @RequestBody UpdateEmployeeContactDto updateEmployeeContactDto) {
        return employeeContactRestService.updateEmployeeContact(id, updateEmployeeContactDto);
    }

    @DeleteMapping("/{id}")
    public DeleteDto deleteEmployeeContact(@PathVariable UUID id) {
        return employeeContactRestService.deleteEmployeeContact(id);
    }
}
