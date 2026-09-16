package com.h4h.employeeportal.absence.core.service.request;

import com.h4h.employeeportal.absence.core.model.AbsenceRequest;

public interface AbsenceRequestCreator {

    AbsenceRequest approveAbsenceRequest(AbsenceRequest absenceRequest);
}
