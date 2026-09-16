package com.h4h.employeeportal.employee.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum EmployeeContactTypeEnum {
    PERSONAL(1),
    CORPORATE(2);

    private final int id;

    public static EmployeeContactTypeEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(contactType -> contactType.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown contactType id: " + id));
    }
}
