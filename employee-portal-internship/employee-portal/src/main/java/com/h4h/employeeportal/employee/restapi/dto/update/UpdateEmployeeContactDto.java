package com.h4h.employeeportal.employee.restapi.dto.update;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactStatusEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeContactDto {

    private UUID uuid;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Contact type is required")
    private EmployeeContactTypeEnum contactType;

    @NotNull(message = "Contact status is required")
    private EmployeeContactStatusEnum contactStatus;
}
