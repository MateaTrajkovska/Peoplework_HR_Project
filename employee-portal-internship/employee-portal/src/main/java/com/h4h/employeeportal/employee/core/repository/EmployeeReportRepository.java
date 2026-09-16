package com.h4h.employeeportal.employee.core.repository;

import com.h4h.employeeportal.employee.core.model.EmployeeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeReportRepository extends JpaRepository<EmployeeReport, UUID> {
    Optional<EmployeeReport> findEmployeeReportByEmployeeUuid(UUID employeeUuid);
}
