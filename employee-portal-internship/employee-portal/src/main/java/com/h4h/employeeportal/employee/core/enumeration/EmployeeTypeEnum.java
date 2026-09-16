package com.h4h.employeeportal.employee.core.enumeration;

import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EmployeeTypeEnum {
    FULL_TIME(1),
    PART_TIME(2),
    EXTERNAL(3),
    INTERN(4);

    private final int id;

    public static EmployeeTypeEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(employeeTypeEnum -> employeeTypeEnum.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Unknown EmployeeType id: " + id));
    }
}
