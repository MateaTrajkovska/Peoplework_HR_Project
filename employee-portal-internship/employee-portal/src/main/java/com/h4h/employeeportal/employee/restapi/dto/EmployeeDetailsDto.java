package com.h4h.employeeportal.employee.restapi.dto;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeGenderEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeStatusEnum;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class EmployeeDetailsDto {
    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String personalId;
    private EmployeeStatusEnum status;
    private Long employeeNumber;
    private EmployeeGenderEnum gender;
    private String nationality;

    private List<EmployeeContactDto> employeeContacts;

    private EmployeeAddressDto employeeAddress;

    private EmployeeFinanceDto employeeFinance;

    private EmployeeInfoDto employeeInfo;

    private EmployeeReportDto employeeReport;
}
