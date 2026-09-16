package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.BankStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeFinance;
import com.h4h.employeeportal.employee.core.model.QEmployeeFinance;
import com.h4h.employeeportal.employee.core.repository.EmployeeFinanceRepository;
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
public class EmployeeFinanceService {

    private final EmployeeFinanceRepository employeeFinanceRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public EmployeeFinance createEmployeeFinance(EmployeeFinance employeeFinance) {
        return employeeFinanceRepository.save(employeeFinance);
    }

    @Transactional(readOnly = true)
    public EmployeeFinance getEmployeeFinanceById(UUID employeeFinanceUuid) {
        return employeeFinanceRepository
                .findById(employeeFinanceUuid)
                .orElseThrow(() -> new ResourceNotFoundException("employee.finance.notFound"));
    }

    @Transactional(readOnly = true)
    public List<EmployeeFinance> getAllEmployeeFinances() {
        return employeeFinanceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EmployeeFinance getEmployeeFinanceByEmployeeUuid(UUID employeeUuid) {
        return employeeFinanceRepository
                .findEmployeeByEmployeeUuid(employeeUuid)
                .orElseThrow(() -> new ResourceNotFoundException("employee.finance.notFound"));
    }

    @Transactional
    public EmployeeFinance updateEmployeeFinance(UUID employeeFinanceUuid, EmployeeFinance updatedEmployeeFinance) {
        EmployeeFinance existingEmployeeFinance = getEmployeeFinanceById(employeeFinanceUuid);
        BeanUtils.copyProperties(updatedEmployeeFinance, existingEmployeeFinance, "uuid", "employeeUuid");
        return employeeFinanceRepository.save(existingEmployeeFinance);
    }

    @Transactional
    public UUID deleteEmployeeFinance(UUID employeeFinanceUuid) {
        QEmployeeFinance employeeFinance = QEmployeeFinance.employeeFinance;

        long rowsUpdated = queryFactory
                .update(employeeFinance)
                .where(employeeFinance.uuid.eq(employeeFinanceUuid))
                .set(employeeFinance.bankStatus, BankStatusEnum.INACTIVE)
                .execute();

        if (rowsUpdated == 0) {
            throw new ResourceNotFoundException("employee.finance.notFound");
        }
        return employeeFinanceUuid;
    }
}
