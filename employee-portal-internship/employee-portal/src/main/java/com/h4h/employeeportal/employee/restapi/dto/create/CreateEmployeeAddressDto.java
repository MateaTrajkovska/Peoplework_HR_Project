package com.h4h.employeeportal.employee.restapi.dto.create;

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
public class CreateEmployeeAddressDto {

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Municipality is required")
    private String municipality;

    @NotBlank(message = "Street is required")
    private String street;
}
