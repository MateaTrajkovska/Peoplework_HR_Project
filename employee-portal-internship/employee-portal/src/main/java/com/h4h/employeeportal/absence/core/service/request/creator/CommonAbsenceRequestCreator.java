package com.h4h.employeeportal.absence.core.service.request.creator;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceRequestStatusEnum;
import com.h4h.employeeportal.absence.core.model.AbsenceRequest;
import com.h4h.employeeportal.absence.core.service.request.AbsenceRequestCreator;
import org.springframework.stereotype.Component;

@Component
public class CommonAbsenceRequestCreator implements AbsenceRequestCreator {

    @Override
    public AbsenceRequest approveAbsenceRequest(AbsenceRequest absenceRequest) {
        absenceRequest.setDaysUsed(0);
        absenceRequest.setDaysUsedFromPreviousYear(0);
        absenceRequest.setDaysUsedFromCurrentYear(0);
        absenceRequest.setAbsenceRequestStatus(
                AbsenceRequestStatusEnum.APPROVED
        );

        return absenceRequest;
    }
}