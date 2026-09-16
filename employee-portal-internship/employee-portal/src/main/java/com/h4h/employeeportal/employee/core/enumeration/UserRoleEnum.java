package com.h4h.employeeportal.employee.core.enumeration;

import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum UserRoleEnum {
    ADMIN(1),
    GENERAL(2);

    private final int id;

    public static UserRoleEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(userRoleEnum -> userRoleEnum.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Unknown UserRole id: " + id));
    }
}
