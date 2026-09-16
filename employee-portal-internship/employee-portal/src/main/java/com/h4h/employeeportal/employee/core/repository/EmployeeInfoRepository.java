package com.h4h.employeeportal.employee.core.repository;

import com.h4h.employeeportal.employee.core.model.EmployeeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, UUID> {
    Optional<EmployeeInfo> findEmployeeInfoByEmployeeUuid(UUID employeeUuid);
}
