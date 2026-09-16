package com.h4h.employeeportal.absence.core.service;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceStatusEnum;
import com.h4h.employeeportal.absence.core.model.Absence;
import com.h4h.employeeportal.absence.core.repository.AbsenceRepository;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeDegreeEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeInfo;
import com.h4h.employeeportal.employee.core.service.EmployeeInfoService;
import com.h4h.employeeportal.shared.exception.ResourceAlreadyExistsException;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AbsenceService {

    private static final int BASE_MAX_DAYS = 20;
    private static final int VALID_FROM_MONTH = 1;
    private static final int VALID_FROM_DAY = 1;
    private static final int VALID_TO_MONTH = 6;
    private static final int VALID_TO_DAY = 30;

    private final AbsenceRepository absenceRepository;
    private final EmployeeInfoService employeeInfoService;

    @Transactional
    public Absence createAbsence(UUID employeeUuid, Integer year) {
        LocalDate validFrom = LocalDate.of(year, VALID_FROM_MONTH, VALID_FROM_DAY);

        if (absenceRepository.existsByEmployeeUuidAndValidFrom(employeeUuid, validFrom)) {
            throw new ResourceAlreadyExistsException("absence.alreadyExists");
        }
        EmployeeInfo employeeInfo = employeeInfoService.getEmployeeInfoByEmployeeUuid(employeeUuid);

        // CRON implementation needs to be added

        int maxDays = BASE_MAX_DAYS
                + calculateExperienceBonus(employeeInfo, year)
                + calculateDegreeBonus(employeeInfo.getDegree());

        Absence absence = Absence.builder()
                .maxDays(maxDays)
                .usedDays(0)
                .absenceStatus(AbsenceStatusEnum.ACTIVE)
                .validFrom(validFrom)
                .validTo(LocalDate.of(year + 1, VALID_TO_MONTH, VALID_TO_DAY))
                .employeeUuid(employeeUuid)
                .build();

        return absenceRepository.save(absence);
    }

    @Transactional(readOnly = true)
    public List<Absence> getAllAbsences() {
        return absenceRepository.findAllByAbsenceStatus(
                AbsenceStatusEnum.ACTIVE
        );
    }
    @Transactional(readOnly = true)
    public Absence getActiveAbsenceById(UUID absenceUuid) {
        return absenceRepository
                .findByUuidAndAbsenceStatus(
                        absenceUuid,
                        AbsenceStatusEnum.ACTIVE
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "absence.notFound"
                        )
                );
    }

    @Transactional(readOnly = true)
    public Absence getAbsenceById(UUID absenceUuid) {
        return absenceRepository
                .findById(absenceUuid)
                .orElseThrow(() -> new ResourceNotFoundException("absence.notFound"));
    }

    @Transactional
    public Absence updateAbsenceStatus(UUID absenceUuid, Absence updatedAbsence) {
        Absence existingAbsence = getAbsenceById(absenceUuid);

        existingAbsence.setAbsenceStatus(updatedAbsence.getAbsenceStatus());

        return absenceRepository.save(existingAbsence);
    }

    @Transactional
    public Absence updateUsedDays(UUID absenceUuid, int additionalDays) {
        Absence absence = getAbsenceByIdForUpdate(absenceUuid);

        if (absence.getAbsenceStatus()
                != AbsenceStatusEnum.ACTIVE) {
            throw new IllegalStateException("absence.inactive");
        }
        if (additionalDays <= 0) {
            throw new IllegalArgumentException("absence.additionalDays.invalid");
        }

        int newUsedDays = absence.getUsedDays() + additionalDays;

        if (newUsedDays > absence.getMaxDays()) {
            throw new IllegalArgumentException("absence.insufficientDays");
        }

        absence.setUsedDays(newUsedDays);

        return absenceRepository.save(absence);
    }

    @Transactional
    public UUID deleteAbsence(UUID absenceUuid) {
        Absence absence = getAbsenceById(absenceUuid);

        if (absence.getAbsenceStatus() == AbsenceStatusEnum.INACTIVE) {
            throw new IllegalStateException("absence.alreadyInactive");
        }

        absence.setAbsenceStatus(AbsenceStatusEnum.INACTIVE);

        absenceRepository.save(absence);

        return absenceUuid;
    }

    private int calculateDegreeBonus(EmployeeDegreeEnum degree) {
        return switch (degree) {
            case NONE -> 0;
            case BACHELORS -> 1;
            case MASTERS -> 2;
            case PHD -> 3;
        };
    }

    private int calculateExperienceBonus(EmployeeInfo employeeInfo, Integer year) {
        LocalDate calculationDate = LocalDate.of(year, VALID_FROM_MONTH, VALID_FROM_DAY);
        LocalDate startDate = employeeInfo.getStartDate();

        if (startDate == null) {
            throw new IllegalStateException("employee.startDate.required");
        }

        long companyExperienceMonths = 0;

        if (!startDate.isAfter(calculationDate)) {
            companyExperienceMonths =
                    ChronoUnit.MONTHS.between(
                            startDate,
                            calculationDate
                    );
        }

        Period priorExperience =
                employeeInfo.getPriorExperience();

        long priorExperienceMonths = 0;

        if (priorExperience != null) {
            priorExperienceMonths =
                    priorExperience.getYears() * 12L
                            + priorExperience.getMonths();
        }

        long totalExperienceMonths =
                companyExperienceMonths
                        + Math.max(priorExperienceMonths, 0);

        return (int) (totalExperienceMonths / 60);
    }

    @Transactional(readOnly = true)
    public Absence getAbsenceByEmployeeAndYear(UUID employeeUuid, Integer year) {
        LocalDate validFrom = LocalDate.of(year, VALID_FROM_MONTH, VALID_FROM_DAY);
        return absenceRepository
                .findByEmployeeUuidAndValidFrom(employeeUuid, validFrom)
                .orElseThrow(() -> new ResourceNotFoundException("absence.notFound"));
    }

    @Transactional
    public void reverseUsedDays(UUID absenceUuid, int days) {
        Absence absence = getAbsenceByIdForUpdate(absenceUuid);

        if (days <= 0) {
            throw new IllegalArgumentException("absence.days.invalid");
        }

        if (days > absence.getUsedDays()) {
            throw new IllegalStateException("absence.reverseDays.exceedsUsedDays");
        }

        int newUsedDays = absence.getUsedDays() - days;

        absence.setUsedDays(newUsedDays);
        absenceRepository.save(absence);
    }

    @Transactional
    public Absence getAbsenceByIdForUpdate(UUID absenceUuid) {
        return absenceRepository.findByUuidForUpdate(absenceUuid)
                .orElseThrow(() -> new ResourceNotFoundException("absence.notFound"));
    }

    @Transactional
    public Optional<Absence> findAbsenceByEmployeeAndYearForUpdate(UUID employeeUuid, Integer year) {
        LocalDate validFrom = LocalDate.of(year, VALID_FROM_MONTH, VALID_FROM_DAY);
        return absenceRepository.findByEmployeeUuidAndValidFromForUpdate(employeeUuid, validFrom);
    }
}
