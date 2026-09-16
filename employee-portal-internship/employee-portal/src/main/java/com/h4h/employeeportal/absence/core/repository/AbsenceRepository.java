package com.h4h.employeeportal.absence.core.repository;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceStatusEnum;
import com.h4h.employeeportal.absence.core.model.Absence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, UUID> {
    Optional<Absence> findByEmployeeUuidAndValidFrom(UUID employeeUuid, LocalDate validFrom);
    boolean existsByEmployeeUuidAndValidFrom(UUID employeeUuid, LocalDate validFrom);
    List<Absence> findAllByAbsenceStatus(AbsenceStatusEnum absenceStatus);
    Optional<Absence> findByUuidAndAbsenceStatus(UUID uuid, AbsenceStatusEnum absenceStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Absence a WHERE a.uuid = :uuid")
    Optional<Absence> findByUuidForUpdate(@Param("uuid") UUID uuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a FROM Absence a
        WHERE a.employeeUuid = :employeeUuid
        AND a.validFrom = :validFrom
        """)
    Optional<Absence> findByEmployeeUuidAndValidFromForUpdate(
            @Param("employeeUuid") UUID employeeUuid,
            @Param("validFrom") LocalDate validFrom
    );
}
