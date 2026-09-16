package com.h4h.employeeportal.employee.restapi.dto;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeStatusEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeListDto {

    private UUID id;
    private Long employeeNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String municipality;
    private EmployeeStatusEnum status;
}
