package com.h4h.employeeportal.employee.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAddressDto {

    private String id;
    private String employeeId;
    private String postalCode;
    private String street;
    private String municipality;
}
