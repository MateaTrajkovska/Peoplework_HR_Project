package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeStatusEnum;
import com.h4h.employeeportal.employee.core.model.Employee;
import com.h4h.employeeportal.employee.core.model.QEmployee;
import com.h4h.employeeportal.employee.core.repository.EmployeeRepository;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public Employee createEmployee(Employee employee) {

        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(UUID employeeUuid) {
        return employeeRepository
                .findById(employeeUuid)
                .orElseThrow(() -> new ResourceNotFoundException("employee.notFound"));
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public Employee updateEmployee(UUID employeeUuid, Employee updatedEmployee) {
        Employee existingEmployee = getEmployeeById(employeeUuid);
        BeanUtils.copyProperties(updatedEmployee, existingEmployee, "uuid", "employeeNumber");
        return employeeRepository.save(existingEmployee);
    }

    @Transactional
    public UUID deleteEmployee(UUID employeeUuid) {
        QEmployee employee = QEmployee.employee;

        long rowsUpdated = queryFactory
                .update(employee)
                .where(employee.uuid.eq(employeeUuid))
                .set(employee.status, EmployeeStatusEnum.ARCHIVED)
                .execute();

        if (rowsUpdated == 0) {
            throw new ResourceNotFoundException("employee.notFound");
        }
        return employeeUuid;
    }
}
