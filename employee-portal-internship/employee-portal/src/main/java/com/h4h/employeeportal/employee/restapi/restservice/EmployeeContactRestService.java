package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.EmployeeContact;
import com.h4h.employeeportal.employee.core.service.EmployeeContactService;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.mapper.EmployeeContactMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeContactRestService {

    private final EmployeeContactService employeeContactService;
    private final EmployeeContactMapper employeeContactMapper;

    public EmployeeContactDto createEmployeeContact(CreateEmployeeContactDto createEmployeeContactDto) {
        EmployeeContact employeeContact = employeeContactService.createEmployeeContact(
                employeeContactMapper.toEntityCreate(createEmployeeContactDto));
        return employeeContactMapper.toDto(employeeContact);
    }

    public List<EmployeeContactDto> getAllEmployeeContacts() {
        return employeeContactMapper.toDtoList(employeeContactService.getAllEmployeeContacts());
    }

    public EmployeeContactDto getEmployeeContactById(UUID employeeContactUuid) {
        return employeeContactMapper.toDto(employeeContactService.getEmployeeContactById(employeeContactUuid));
    }

    public EmployeeContactDto updateEmployeeContact(
            UUID employeeContactUuid, UpdateEmployeeContactDto updateEmployeeContactDto) {
        EmployeeContact updatedEmployeeContact = employeeContactService.updateEmployeeContact(
                employeeContactUuid, employeeContactMapper.toEntityUpdate(updateEmployeeContactDto));
        return employeeContactMapper.toDto(updatedEmployeeContact);
    }

    public DeleteDto deleteEmployeeContact(UUID employeeContactUuid) {
        return employeeContactMapper.toDeletedDto(employeeContactService.deleteEmployeeContact(employeeContactUuid));
    }
}
