package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.UserRoleEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRoleEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRoleEnum userRole) {
        return userRole == null ? null : userRole.getId();
    }

    @Override
    public UserRoleEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : UserRoleEnum.fromId(id);
    }
}
