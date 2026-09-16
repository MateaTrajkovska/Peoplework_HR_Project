package com.h4h.employeeportal.absence.core.service.request.creator;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceRequestStatusEnum;
import com.h4h.employeeportal.absence.core.enumeration.AbsenceStatusEnum;
import com.h4h.employeeportal.absence.core.model.Absence;
import com.h4h.employeeportal.absence.core.model.AbsenceRequest;
import com.h4h.employeeportal.absence.core.service.AbsenceService;
import com.h4h.employeeportal.absence.core.service.request.AbsenceRequestCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class VacationAbsenceRequestCreator implements AbsenceRequestCreator {

    private final AbsenceService absenceService;
    private static final int PREVIOUS_YEAR_VALID_TO_MONTH = 6;
    private static final int PREVIOUS_YEAR_VALID_TO_DAY = 30;

    @Override
    @Transactional
    public AbsenceRequest approveAbsenceRequest(AbsenceRequest absenceRequest) {

        if (absenceRequest.getFromDate() == null || absenceRequest.getToDate() == null) {
            throw new IllegalArgumentException("absenceRequest.dates.required");
        }

        if (absenceRequest.getFromDate().isAfter(absenceRequest.getToDate())) {
            throw new IllegalArgumentException("absenceRequest.dates.invalidRange");
        }

        Absence currentAbsence = absenceService.getAbsenceByIdForUpdate(absenceRequest.getAbsenceUuid());

        if (absenceRequest.getFromDate().isBefore(currentAbsence.getValidFrom())) {
            throw new IllegalArgumentException("absenceRequest.fromDate.beforeValidity");
        }

        if (absenceRequest.getToDate().isAfter(currentAbsence.getValidTo())) {
            throw new IllegalArgumentException("absenceRequest.toDate.afterValidity");
        }

        LocalDate fromDate = absenceRequest.getFromDate();
        LocalDate toDate = absenceRequest.getToDate();

        int daysUsed = calculate(fromDate, toDate);

        if (daysUsed == 0) {
            throw new IllegalArgumentException("absenceRequest.workingDays.required");
        }

        LocalDate previousYearValidTo = LocalDate.of(currentAbsence.getYear(), PREVIOUS_YEAR_VALID_TO_MONTH, PREVIOUS_YEAR_VALID_TO_DAY);

        int daysEligibleFromPreviousYear = 0;

        if (!fromDate.isAfter(previousYearValidTo)) {
            LocalDate previousYearPeriodEnd =
                    toDate.isBefore(previousYearValidTo)
                            ? toDate
                            : previousYearValidTo;

            daysEligibleFromPreviousYear = calculate(
                    fromDate,
                    previousYearPeriodEnd
            );
        }

        int daysFromPreviousYear = 0;

        Absence previousAbsence = absenceService.findAbsenceByEmployeeAndYearForUpdate(
                currentAbsence.getEmployeeUuid(),
                currentAbsence.getYear() - 1
        ).orElse(null);

        if (previousAbsence != null
                && previousAbsence.getAbsenceStatus()
                == AbsenceStatusEnum.ACTIVE
                && daysEligibleFromPreviousYear > 0) {

            int previousAvailableDays =
                    previousAbsence.getMaxDays()
                            - previousAbsence.getUsedDays();

            daysFromPreviousYear = Math.min(
                    daysEligibleFromPreviousYear,
                    previousAvailableDays
            );
        }

        int daysFromCurrentYear =
                daysUsed - daysFromPreviousYear;

        int currentAvailableDays =
                currentAbsence.getMaxDays()
                        - currentAbsence.getUsedDays();

        if (daysFromCurrentYear > currentAvailableDays) {
            throw new IllegalArgumentException("absence.insufficientDays");
        }

        if (daysFromPreviousYear > 0) {
            absenceService.updateUsedDays(
                    previousAbsence.getUuid(),
                    daysFromPreviousYear
            );
        }

        if (daysFromCurrentYear > 0) {
            absenceService.updateUsedDays(
                    currentAbsence.getUuid(),
                    daysFromCurrentYear
            );
        }

        absenceRequest.setDaysUsed(daysUsed);
        absenceRequest.setDaysUsedFromPreviousYear(daysFromPreviousYear);
        absenceRequest.setDaysUsedFromCurrentYear(daysFromCurrentYear);
        absenceRequest.setAbsenceRequestStatus(AbsenceRequestStatusEnum.APPROVED);

        return absenceRequest;
    }

    private int calculate(LocalDate fromDate, LocalDate toDate) {
        int workingDays = 0;

        LocalDate currentDate = fromDate;

        while (!currentDate.isAfter(toDate)) {
            if (isWorkingDay(currentDate)) {
                workingDays++;
            }

            currentDate = currentDate.plusDays(1);
        }

        return workingDays;
    }

    private boolean isWorkingDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
}