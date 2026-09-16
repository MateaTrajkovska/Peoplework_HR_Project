package com.h4h.employeeportal.absence.core.converter;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceRequestStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AbsenceRequestStatusEnumConverter implements AttributeConverter<AbsenceRequestStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AbsenceRequestStatusEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public AbsenceRequestStatusEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : AbsenceRequestStatusEnum.fromId(id);
    }
}
