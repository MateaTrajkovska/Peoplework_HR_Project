package com.h4h.employeeportal.employee.restapi.dto;

import com.h4h.employeeportal.employee.core.enumeration.BankStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFinanceDto {

    private UUID id;
    private UUID employeeId;
    private Long bankAccount;
    private BankStatusEnum bankStatus;
}
