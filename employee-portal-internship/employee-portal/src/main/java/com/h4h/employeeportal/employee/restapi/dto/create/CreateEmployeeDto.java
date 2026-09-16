package com.h4h.employeeportal.employee.restapi.dto.create;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeDegreeEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeGenderEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeePositionEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeTypeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDto {

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

    private UUID userId;

    // EmployeeContact
    @Valid
    private List<CreateEmployeeContactDto> employeeContacts;

    // EmployeeAddress
    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Municipality is required")
    private String municipality;

    @NotBlank(message = "Street is required")
    private String street;

    // EmployeeFinance
    @NotNull(message = "Bank account is required")
    private Long bankAccount;

    // EmployeeInfo
    @NotNull(message = "Date of birth is required is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Employee type is required")
    private EmployeeTypeEnum employeeType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Degree is required")
    private EmployeeDegreeEnum degree;

    @NotBlank(message = "Identification number is required")
    private String identificationNumber;

    private Period priorExperience;

    @NotNull(message = "Position is required")
    private EmployeePositionEnum position;

    // EmployeeReport
    @Size(max = 1000, message = "Internal note cannot exceed 1000 characters")
    private String internalNote;

}
