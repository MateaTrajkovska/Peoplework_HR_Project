package com.h4h.employeeportal.absence.core.service.request;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceTypeEnum;
import com.h4h.employeeportal.absence.core.model.AbsenceRequest;
import com.h4h.employeeportal.absence.core.service.request.creator.CommonAbsenceRequestCreator;
import com.h4h.employeeportal.absence.core.service.request.creator.VacationAbsenceRequestCreator;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AbsenceRequestFactory {

    private final VacationAbsenceRequestCreator vacationAbsenceRequestCreator;
    private final CommonAbsenceRequestCreator commonAbsenceRequestCreator;

    public AbsenceRequestCreator getCreator(AbsenceTypeEnum type) {
        return switch (type) {
            case VACATION -> vacationAbsenceRequestCreator;

            case MATERNITY_LEAVE,
                 PATERNITY_LEAVE,
                 SICK_LEAVE,
                 MEDICAL_LEAVE -> commonAbsenceRequestCreator;

            default -> throw new ResourceNotFoundException("type.notFound");
        };
    }

    public AbsenceRequest approveAbsenceRequest(AbsenceRequest absenceRequest) {
        return getCreator(absenceRequest.getType()).approveAbsenceRequest(absenceRequest);
    }
}