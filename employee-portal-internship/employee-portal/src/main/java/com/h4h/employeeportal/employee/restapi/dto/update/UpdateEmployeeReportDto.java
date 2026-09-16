package com.h4h.employeeportal.employee.restapi.dto.update;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeReportStatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeReportDto {

    private String internalNote;

    @NotBlank(message = "Report status is required")
    private EmployeeReportStatusEnum reportStatus;
}
