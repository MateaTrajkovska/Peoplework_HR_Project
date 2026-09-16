package com.h4h.employeeportal.employee.restapi.dto;

import com.h4h.employeeportal.employee.core.enumeration.LanguageEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserRoleEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserStatusEnum;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;

    private String username;

    private UserRoleEnum role;

    private UserStatusEnum status;

    private LanguageEnum language;
}
