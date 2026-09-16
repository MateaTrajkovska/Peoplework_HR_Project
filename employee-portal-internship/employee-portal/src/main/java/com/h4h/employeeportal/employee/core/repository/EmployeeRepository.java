package com.h4h.employeeportal.employee.core.repository;

import com.h4h.employeeportal.employee.core.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {}
