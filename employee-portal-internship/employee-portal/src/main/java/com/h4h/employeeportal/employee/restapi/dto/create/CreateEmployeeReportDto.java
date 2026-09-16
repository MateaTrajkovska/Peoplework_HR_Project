package com.h4h.employeeportal.employee.restapi.dto.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeReportDto {

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @Size(max = 1000, message = "Internal note must not exceed 1000 characters")
    private String internalNote;
}
