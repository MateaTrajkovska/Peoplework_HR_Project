package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ContactStatusConverter implements AttributeConverter<EmployeeContactStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeContactStatusEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public EmployeeContactStatusEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : EmployeeContactStatusEnum.fromId(id);
    }
}
