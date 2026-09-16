package com.h4h.employeeportal.employee.restapi.dto;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeReportStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeReportDto {

    private String id;
    private String employeeId;
    private String internalNote;
    private EmployeeReportStatusEnum reportStatus;
}
