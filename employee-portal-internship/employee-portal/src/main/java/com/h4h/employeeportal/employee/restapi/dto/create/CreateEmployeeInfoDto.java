package com.h4h.employeeportal.employee.restapi.dto.create;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeDegreeEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeePositionEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeInfoDto {

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotNull(message = "Date of birth is required")
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
}
