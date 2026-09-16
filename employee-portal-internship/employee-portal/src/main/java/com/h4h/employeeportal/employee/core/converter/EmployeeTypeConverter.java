package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeeTypeConverter implements AttributeConverter<EmployeeTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeTypeEnum type) {
        return type == null ? null : type.getId();
    }

    @Override
    public EmployeeTypeEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : EmployeeTypeEnum.fromId(id);
    }
}
