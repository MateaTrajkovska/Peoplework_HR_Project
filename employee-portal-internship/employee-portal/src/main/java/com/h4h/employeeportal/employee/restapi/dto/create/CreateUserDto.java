package com.h4h.employeeportal.employee.restapi.dto.create;

import com.h4h.employeeportal.employee.core.enumeration.LanguageEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserRoleEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "User role is required")
    private UserRoleEnum role;

    private UserStatusEnum status;

    private LanguageEnum language;
}
