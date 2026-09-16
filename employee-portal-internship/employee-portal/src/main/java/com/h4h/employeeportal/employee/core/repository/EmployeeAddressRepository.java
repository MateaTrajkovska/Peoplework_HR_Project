package com.h4h.employeeportal.employee.core.repository;

import com.h4h.employeeportal.employee.core.model.EmployeeAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeAddressRepository extends JpaRepository<EmployeeAddress, UUID> {
    Optional<EmployeeAddress> findEmployeeAddressByEmployeeUuid(UUID employeeUuid);
}
