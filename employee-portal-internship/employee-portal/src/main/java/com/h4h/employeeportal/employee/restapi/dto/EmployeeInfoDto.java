package com.h4h.employeeportal.employee.restapi.dto;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeDegreeEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeInfoStatusEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeePositionEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInfoDto {

    private String id;
    private String employeeId;
    private LocalDate dateOfBirth;
    private EmployeeTypeEnum employeeType;
    private LocalDate startDate;
    private LocalDate endDate;
    private EmployeeDegreeEnum degree;
    private String identificationNumber;
    private Period priorExperience;
    private EmployeePositionEnum position;
    private EmployeeInfoStatusEnum infoStatus;
}
