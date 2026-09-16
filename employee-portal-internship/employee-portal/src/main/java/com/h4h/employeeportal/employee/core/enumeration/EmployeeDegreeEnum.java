package com.h4h.employeeportal.employee.core.enumeration;

import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum EmployeeDegreeEnum {
    NONE(0),
    BACHELORS(1),
    MASTERS(2),
    PHD(3);

    private final int id;

    public static EmployeeDegreeEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(employeeDegreeEnum -> employeeDegreeEnum.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Unknown EmployeeDegree id: " + id));
    }
}
