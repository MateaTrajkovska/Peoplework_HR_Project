package com.h4h.employeeportal.absence.core.repository;

import com.h4h.employeeportal.absence.core.model.AbsenceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, UUID> {
    @Query(
            value = "SELECT nextval('seq_absence_decision_number')",
            nativeQuery = true
    )
    Long getNextDecisionNumber();
}
