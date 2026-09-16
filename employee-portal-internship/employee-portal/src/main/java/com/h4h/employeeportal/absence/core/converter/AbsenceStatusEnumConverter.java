package com.h4h.employeeportal.absence.core.converter;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AbsenceStatusEnumConverter implements AttributeConverter<AbsenceStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AbsenceStatusEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public AbsenceStatusEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : AbsenceStatusEnum.fromId(id);
    }
}
