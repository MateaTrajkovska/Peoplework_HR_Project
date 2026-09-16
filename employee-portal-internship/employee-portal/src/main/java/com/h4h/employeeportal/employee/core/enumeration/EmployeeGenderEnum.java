package com.h4h.employeeportal.employee.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum EmployeeGenderEnum {
    MALE(1),
    FEMALE(2),
    OTHER(3);

    private final int id;

    public static EmployeeGenderEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(employeeGenderEnum -> employeeGenderEnum.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown employeeGender id: " + id));
    }
}
