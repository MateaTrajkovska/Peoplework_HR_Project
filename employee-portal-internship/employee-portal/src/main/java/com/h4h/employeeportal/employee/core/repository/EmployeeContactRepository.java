package com.h4h.employeeportal.employee.core.repository;

import com.h4h.employeeportal.employee.core.model.EmployeeContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeContactRepository extends JpaRepository<EmployeeContact, UUID> {
    List<EmployeeContact> findEmployeeContactsByEmployeeUuid(UUID employeeUuid);
}
