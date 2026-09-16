package com.h4h.employeeportal.employee.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum EmployeeStatusEnum {
    ACTIVE(1),
    ARCHIVED(-1);

    private final int id;

    public static EmployeeStatusEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(employeeStatusEnum -> employeeStatusEnum.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown employeeStatusId: " + id));
    }
}
