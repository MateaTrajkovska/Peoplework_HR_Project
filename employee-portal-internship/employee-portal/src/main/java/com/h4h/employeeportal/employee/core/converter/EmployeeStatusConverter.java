package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeeStatusConverter implements AttributeConverter<EmployeeStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeStatusEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public EmployeeStatusEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : EmployeeStatusEnum.fromId(id);
    }
}
