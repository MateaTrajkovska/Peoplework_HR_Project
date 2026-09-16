package com.h4h.employeeportal.employee.core.repository;

import com.h4h.employeeportal.employee.core.model.EmployeeFinance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeFinanceRepository extends JpaRepository<EmployeeFinance, UUID> {
    Optional<EmployeeFinance> findEmployeeByEmployeeUuid(UUID employeeUuid);
}
