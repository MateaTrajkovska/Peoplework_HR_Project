package com.h4h.employeeportal.employee.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum EmployeeContactStatusEnum {
    ACTIVE(1),
    INACTIVE(-1);

    private final int id;

    public static EmployeeContactStatusEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(contactStatus -> contactStatus.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown contactStatus id: " + id));
    }
}
