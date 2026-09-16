package com.h4h.employeeportal.employee.restapi.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeFinanceDto {

    @NotNull(message = "Employee UUID is required")
    private UUID employeeId;

    @NotNull(message = "Bank account is required")
    private Long bankAccount;
}
