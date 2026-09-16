package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.UserStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserStatusEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public UserStatusEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : UserStatusEnum.fromId(id);
    }
}
