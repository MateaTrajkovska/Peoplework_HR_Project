package com.h4h.employeeportal.absence.core.service;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceRequestStatusEnum;
import com.h4h.employeeportal.absence.core.enumeration.AbsenceStatusEnum;
import com.h4h.employeeportal.absence.core.model.AbsenceRequest;
import com.h4h.employeeportal.absence.core.repository.AbsenceRequestRepository;
import com.h4h.employeeportal.absence.core.service.request.AbsenceRequestFactory;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.h4h.employeeportal.absence.core.enumeration.AbsenceTypeEnum;
import com.h4h.employeeportal.absence.core.model.Absence;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AbsenceRequestService {

    private final AbsenceRequestRepository absenceRequestRepository;
    private final AbsenceRequestFactory absenceRequestFactory;
    private final AbsenceService absenceService;

    @Transactional
    public AbsenceRequest createAbsenceRequest(AbsenceRequest absenceRequest) {
        Absence absence = absenceService.getAbsenceById(
                absenceRequest.getAbsenceUuid()
        );

        if (absence.getAbsenceStatus() != AbsenceStatusEnum.ACTIVE) {
            throw new IllegalStateException(
                    "absence.inactive"
            );
        }

        validateRequestDates(absenceRequest, absence);

        absenceRequest.setDecisionNumber(generateDecisionNumber(absenceRequest));
        absenceRequest.setDaysUsedFromPreviousYear(0);
        absenceRequest.setDaysUsedFromCurrentYear(0);
        absenceRequest.setAbsenceRequestStatus(
                AbsenceRequestStatusEnum.PENDING
        );

        return absenceRequestRepository.save(absenceRequest);
    }
    private void validateRequestDates(AbsenceRequest absenceRequest, Absence absence) {
        if (absenceRequest.getFromDate() == null
                || absenceRequest.getToDate() == null) {
            throw new IllegalArgumentException(
                    "absenceRequest.dates.required"
            );
        }

        if (absenceRequest.getFromDate()
                .isAfter(absenceRequest.getToDate())) {
            throw new IllegalArgumentException(
                    "absenceRequest.dates.invalidRange"
            );
        }

        if (absenceRequest.getFromDate()
                .isBefore(absence.getValidFrom())) {
            throw new IllegalArgumentException(
                    "absenceRequest.fromDate.beforeValidity"
            );
        }

        if (absenceRequest.getToDate()
                .isAfter(absence.getValidTo())) {
            throw new IllegalArgumentException(
                    "absenceRequest.toDate.afterValidity"
            );
        }
    }

    @Transactional
    public AbsenceRequest approveAbsenceRequest(UUID absenceRequestUuid) {
        AbsenceRequest absenceRequest = getAbsenceRequestById(absenceRequestUuid);

        if (absenceRequest.getAbsenceRequestStatus() != AbsenceRequestStatusEnum.PENDING) {
            throw new IllegalStateException("absenceRequest.notPending");
        }

        AbsenceRequest approvedAbsenceRequest = absenceRequestFactory.approveAbsenceRequest(absenceRequest);

        return absenceRequestRepository.save(approvedAbsenceRequest);
    }

    @Transactional
    public AbsenceRequest denyAbsenceRequest(UUID absenceRequestUuid) {
        AbsenceRequest absenceRequest = getAbsenceRequestById(absenceRequestUuid);

        if (absenceRequest.getAbsenceRequestStatus() != AbsenceRequestStatusEnum.PENDING) {
            throw new IllegalStateException("absenceRequest.notPending");
        }

        absenceRequest.setAbsenceRequestStatus(AbsenceRequestStatusEnum.DENIED);

        return absenceRequestRepository.save(absenceRequest);
    }

    private String generateDecisionNumber(AbsenceRequest absenceRequest) {
        if (absenceRequest.getDecisionDate() == null) {
            throw new IllegalArgumentException("absenceRequest.decisionDate.required");
        }

        if (absenceRequest.getType() == null) {
            throw new IllegalArgumentException("absenceRequest.type.required");
        }

        Long sequenceNumber = absenceRequestRepository.getNextDecisionNumber();

        String abbreviation = absenceRequest.getType().getAbbreviation().toUpperCase();

        int year = absenceRequest.getDecisionDate().getYear();

        int month = absenceRequest.getDecisionDate().getMonthValue();

        return String.format(
                "%s-%04d/%02d-%04d",
                abbreviation,
                year,
                month,
                sequenceNumber
        );
    }

    @Transactional(readOnly = true)
    public List<AbsenceRequest> getAllAbsenceRequests() {
        return absenceRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AbsenceRequest getAbsenceRequestById(UUID absenceRequestUuid) {
        return absenceRequestRepository
                .findById(absenceRequestUuid)
                .orElseThrow(() -> new ResourceNotFoundException("absenceRequest.notFound"));
    }

    @Transactional
    public AbsenceRequest updateAbsenceRequest(UUID absenceRequestUuid, AbsenceRequest updatedAbsenceRequest) {
        AbsenceRequest existingAbsenceRequest = getAbsenceRequestById(absenceRequestUuid);

        if (existingAbsenceRequest.getAbsenceRequestStatus() != AbsenceRequestStatusEnum.PENDING) {
            throw new IllegalStateException("absenceRequest.notPending");
        }

        existingAbsenceRequest.setFromDate(updatedAbsenceRequest.getFromDate());
        existingAbsenceRequest.setToDate(updatedAbsenceRequest.getToDate());

        Absence absence = absenceService.getAbsenceById(existingAbsenceRequest.getAbsenceUuid());
        validateRequestDates(existingAbsenceRequest, absence);

        return absenceRequestRepository.save(existingAbsenceRequest);
    }

    private void reverseVacationDays(AbsenceRequest absenceRequest) {
        int daysFromPreviousYear =
                absenceRequest.getDaysUsedFromPreviousYear() == null
                        ? 0
                        : absenceRequest.getDaysUsedFromPreviousYear();

        int daysFromCurrentYear =
                absenceRequest.getDaysUsedFromCurrentYear() == null
                        ? 0
                        : absenceRequest.getDaysUsedFromCurrentYear();

        Absence currentAbsence = absenceService.getAbsenceById(
                absenceRequest.getAbsenceUuid()
        );

        if (daysFromPreviousYear > 0) {
            Absence previousAbsence =
                    absenceService.getAbsenceByEmployeeAndYear(
                            currentAbsence.getEmployeeUuid(),
                            currentAbsence.getYear() - 1
                    );

            absenceService.reverseUsedDays(
                    previousAbsence.getUuid(),
                    daysFromPreviousYear
            );
        }

        if (daysFromCurrentYear > 0) {
            absenceService.reverseUsedDays(
                    currentAbsence.getUuid(),
                    daysFromCurrentYear
            );
        }
    }

    @Transactional
    public UUID deleteAbsenceRequest(UUID absenceRequestUuid) {
        AbsenceRequest existingAbsenceRequest = getAbsenceRequestById(absenceRequestUuid);

        if (existingAbsenceRequest.getAbsenceRequestStatus() == AbsenceRequestStatusEnum.DENIED) {
            throw new IllegalStateException("absenceRequest.alreadyDenied");
        }

        if (existingAbsenceRequest.getAbsenceRequestStatus() == AbsenceRequestStatusEnum.APPROVED
                && existingAbsenceRequest.getType() == AbsenceTypeEnum.VACATION) {
            reverseVacationDays(existingAbsenceRequest);
        }

        existingAbsenceRequest.setAbsenceRequestStatus(AbsenceRequestStatusEnum.DENIED);
        absenceRequestRepository.save(existingAbsenceRequest);

        return absenceRequestUuid;
    }
}
