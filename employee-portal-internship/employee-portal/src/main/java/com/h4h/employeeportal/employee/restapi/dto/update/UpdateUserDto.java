package com.h4h.employeeportal.employee.restapi.dto.update;

import com.h4h.employeeportal.employee.core.enumeration.LanguageEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserRoleEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

    private String username;

    private String password;

    private UserRoleEnum role;

    private UserStatusEnum status;

    private LanguageEnum language;
}
