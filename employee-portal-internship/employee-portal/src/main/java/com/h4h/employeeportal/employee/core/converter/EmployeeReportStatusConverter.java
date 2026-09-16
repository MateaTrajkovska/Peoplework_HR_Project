package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeReportStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeeReportStatusConverter implements AttributeConverter<EmployeeReportStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeReportStatusEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public EmployeeReportStatusEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : EmployeeReportStatusEnum.fromId(id);
    }
}
