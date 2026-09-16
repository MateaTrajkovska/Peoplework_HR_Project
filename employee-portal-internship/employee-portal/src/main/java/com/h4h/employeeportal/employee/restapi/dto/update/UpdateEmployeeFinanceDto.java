package com.h4h.employeeportal.employee.restapi.dto.update;

import com.h4h.employeeportal.employee.core.enumeration.BankStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeFinanceDto {

    @NotNull(message = "Bank account is required")
    private Long bankAccount;

    @NotNull(message = "Bank status is required")
    private BankStatusEnum bankStatus;
}
