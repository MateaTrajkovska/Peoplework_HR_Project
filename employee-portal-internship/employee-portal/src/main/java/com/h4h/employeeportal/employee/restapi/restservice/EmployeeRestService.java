package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactTypeEnum;
import com.h4h.employeeportal.employee.core.model.*;
import com.h4h.employeeportal.employee.core.service.*;
import com.h4h.employeeportal.employee.restapi.dto.*;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.mapper.*;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeRestService {

    private final EmployeeService employeeService;
    private final EmployeeContactService employeeContactService;
    private final EmployeeAddressService employeeAddressService;
    private final EmployeeFinanceService employeeFinanceService;
    private final EmployeeInfoService employeeInfoService;
    private final EmployeeReportService employeeReportService;

    private final EmployeeMapper employeeMapper;
    private final EmployeeContactMapper employeeContactMapper;
    private final EmployeeAddressMapper employeeAddressMapper;
    private final EmployeeFinanceMapper employeeFinanceMapper;
    private final EmployeeInfoMapper employeeInfoMapper;
    private final EmployeeReportMapper employeeReportMapper;

    @Transactional
    public EmployeeDto createEmployee(CreateEmployeeDto createEmployeeDto) {
        Employee savedEmployee = employeeService.createEmployee(employeeMapper.toEntityCreate(createEmployeeDto));
        UUID employeeUuid = savedEmployee.getUuid();

        for (CreateEmployeeContactDto contactDto : createEmployeeDto.getEmployeeContacts()) {
            EmployeeContact employeeContact = employeeContactMapper.toEntityCreate(contactDto);

            employeeContact.setEmployeeUuid(employeeUuid);
            employeeContactService.createEmployeeContact(employeeContact);
        }

        EmployeeAddress employeeAddress = employeeAddressMapper.toEntityCreateFromEmployee(createEmployeeDto);
        employeeAddress.setEmployeeUuid(employeeUuid);

        EmployeeFinance employeeFinance = employeeFinanceMapper.toEntityCreateFromEmployee(createEmployeeDto);
        employeeFinance.setEmployeeUuid(employeeUuid);

        EmployeeInfo employeeInfo = employeeInfoMapper.toEntityCreateFromEmployee(createEmployeeDto);
        employeeInfo.setEmployeeUuid(employeeUuid);

        EmployeeReport employeeReport = employeeReportMapper.toEntityCreateFromEmployee(createEmployeeDto);
        employeeReport.setEmployeeUuid(employeeUuid);

        employeeAddressService.createEmployeeAddress(employeeAddress);
        employeeFinanceService.createEmployeeFinance(employeeFinance);
        employeeInfoService.createEmployeeInfo(employeeInfo);
        employeeReportService.createEmployeeReport(employeeReport);

        return employeeMapper.toDto(savedEmployee);
    }

    public List<EmployeeListDto> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeListDto> employeeListDto = employeeMapper.toDtoList(employees);

        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            EmployeeListDto employeeDto = employeeListDto.get(i);
            UUID employeeUuid = employee.getUuid();

            List<EmployeeContact> contacts = employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid);
            List<EmployeeContactDto> contactDto = employeeContactMapper.toDtoList(contacts);

            String email = contactDto.stream()
                    .filter(contact -> contact.getContactType() == EmployeeContactTypeEnum.CORPORATE)
                    .map(EmployeeContactDto::getEmail)
                    .findFirst()
                    .orElseGet(() -> contactDto.stream()
                            .filter(contact -> contact.getContactType() == EmployeeContactTypeEnum.PERSONAL)
                            .map(EmployeeContactDto::getEmail)
                            .findFirst()
                            .orElse(null));

            employeeDto.setEmail(email);

            EmployeeAddress address = employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid);
            EmployeeAddressDto addressDto = employeeAddressMapper.toDto(address);

            employeeDto.setMunicipality(addressDto.getMunicipality());
        }

        return employeeListDto;
    }

    public EmployeeDetailsDto getEmployeeById(UUID employeeUuid) {
        Employee employee = employeeService.getEmployeeById(employeeUuid);

        EmployeeDetailsDto employeeDetailsDto = employeeMapper.toDtoDetails(employee);

        employeeDetailsDto.setEmployeeContacts(employeeContactMapper.toDtoList(
                employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid)));

        employeeDetailsDto.setEmployeeAddress(
                employeeAddressMapper.toDto(employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid)));

        employeeDetailsDto.setEmployeeFinance(
                employeeFinanceMapper.toDto(employeeFinanceService.getEmployeeFinanceByEmployeeUuid(employeeUuid)));

        employeeDetailsDto.setEmployeeInfo(
                employeeInfoMapper.toDto(employeeInfoService.getEmployeeInfoByEmployeeUuid(employeeUuid)));

        employeeDetailsDto.setEmployeeReport(
                employeeReportMapper.toDto(employeeReportService.getEmployeeReportByEmployeeUuid(employeeUuid)));

        return employeeDetailsDto;
    }

    @Transactional
    public EmployeeDto updateEmployee(UUID employeeUuid, UpdateEmployeeDto updateEmployeeDto) {
        Employee updatedEmployee =
                employeeService.updateEmployee(employeeUuid, employeeMapper.toEntityUpdate(updateEmployeeDto));

        for (UpdateEmployeeContactDto contactDto : updateEmployeeDto.getEmployeeContacts()) {
            EmployeeContact updatedEmployeeContact = employeeContactMapper.toEntityUpdate(contactDto);

            employeeContactService.updateEmployeeContact(contactDto.getUuid(), updatedEmployeeContact);
        }

        if (updateEmployeeDto.getEmployeeFinance() != null) {
            EmployeeFinance existingFinance = employeeFinanceService.getEmployeeFinanceByEmployeeUuid(employeeUuid);
            EmployeeFinance updatedFinance =
                    employeeFinanceMapper.toEntityUpdate(updateEmployeeDto.getEmployeeFinance());
            employeeFinanceService.updateEmployeeFinance(existingFinance.getUuid(), updatedFinance);
        }

        if (updateEmployeeDto.getEmployeeAddress() != null) {
            EmployeeAddress existingAddress = employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid);
            EmployeeAddress updatedAddress =
                    employeeAddressMapper.toEntityUpdate(updateEmployeeDto.getEmployeeAddress());
            employeeAddressService.updateEmployeeAddress(existingAddress.getUuid(), updatedAddress);
        }

        if (updateEmployeeDto.getEmployeeInfo() != null) {
            EmployeeInfo existingInfo = employeeInfoService.getEmployeeInfoByEmployeeUuid(employeeUuid);
            EmployeeInfo updatedInfo = employeeInfoMapper.toEntityUpdate(updateEmployeeDto.getEmployeeInfo());
            employeeInfoService.updateEmployeeInfo(existingInfo.getUuid(), updatedInfo);
        }

        if (updateEmployeeDto.getEmployeeReportDto() != null) {
            EmployeeReport existingReport = employeeReportService.getEmployeeReportByEmployeeUuid(employeeUuid);
            EmployeeReport updatedReport =
                    employeeReportMapper.toEntityUpdate(updateEmployeeDto.getEmployeeReportDto());
            employeeReportService.updateEmployeeReport(existingReport.getUuid(), updatedReport);
        }

        return employeeMapper.toDto(updatedEmployee);
    }

    public DeleteDto deleteEmployee(UUID employeeUuid) {
        return employeeMapper.toDeletedDto(employeeService.deleteEmployee(employeeUuid));
    }
}
