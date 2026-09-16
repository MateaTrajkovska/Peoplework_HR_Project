package com.h4h.employeeportal.employee.core.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum UserStatusEnum {
    ACTIVE(1),
    ARCHIVED(-1);

    private final int id;

    public static UserStatusEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(userStatusEnum -> userStatusEnum.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown userStatus id : " + id));
    }
}
