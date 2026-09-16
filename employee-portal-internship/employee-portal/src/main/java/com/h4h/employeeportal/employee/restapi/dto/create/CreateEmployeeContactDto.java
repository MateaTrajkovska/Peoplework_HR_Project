package com.h4h.employeeportal.employee.restapi.dto.create;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeContactDto {

    //    @NotNull(message = "Employee ID is required")
    //    private UUID employeeId;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Contact type is required")
    private EmployeeContactTypeEnum contactType;
}
