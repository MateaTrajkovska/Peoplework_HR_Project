package com.h4h.employeeportal.employee.restapi.dto;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeGenderEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String personalId;
    private EmployeeStatusEnum status;
    private Long employeeNumber;
    private EmployeeGenderEnum gender;
    private String nationality;
}
