package com.h4h.employeeportal.employee.restapi.dto.update;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeGenderEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeDto {

    // Employee
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String personalId;

    @NotNull(message = "Gender is required")
    private EmployeeGenderEnum gender;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    @NotNull(message = "Status is required")
    private EmployeeStatusEnum status;

    private UUID userId;

    @Valid
    private List<UpdateEmployeeContactDto> employeeContacts;

    @Valid
    private UpdateEmployeeFinanceDto employeeFinance;

    @Valid
    private UpdateEmployeeAddressDto employeeAddress;

    @Valid
    private UpdateEmployeeInfoDto employeeInfo;

    @Valid
    private UpdateEmployeeReportDto employeeReportDto;
}
