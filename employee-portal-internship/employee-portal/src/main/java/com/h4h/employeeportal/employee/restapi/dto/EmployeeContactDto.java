package com.h4h.employeeportal.employee.restapi.dto;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactStatusEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeContactDto {

    private UUID id;
    private UUID employeeId;
    private String email;
    private String phoneNumber;
    private EmployeeContactTypeEnum contactType;
    private EmployeeContactStatusEnum contactStatus;
}
