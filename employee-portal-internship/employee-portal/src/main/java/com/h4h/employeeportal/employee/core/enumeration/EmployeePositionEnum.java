package com.h4h.employeeportal.employee.core.enumeration;

import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EmployeePositionEnum {
    CEO(1),
    HR(2),
    FE_DEV(3),
    BE_DEV(4),
    QA(5);

    private final int id;

    public static EmployeePositionEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(positionEnum -> positionEnum.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Unknown EmployeePosition id: " + id));
    }
}
