package com.h4h.employeeportal.employee.restapi.dto.update;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeAddressStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeAddressDto {

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Postal code is required")
    private String municipality;

    @NotBlank(message = "Postal code is required")
    private String street;

    @NotNull(message = "Postal code is required")
    private EmployeeAddressStatusEnum status;
}
