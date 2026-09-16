package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeReportStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeReport;
import com.h4h.employeeportal.employee.core.model.QEmployeeReport;
import com.h4h.employeeportal.employee.core.repository.EmployeeReportRepository;
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
public class EmployeeReportService {

    private final EmployeeReportRepository employeeReportRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public EmployeeReport createEmployeeReport(EmployeeReport employeeReport) {

        return employeeReportRepository.save(employeeReport);
    }

    @Transactional(readOnly = true)
    public EmployeeReport getEmployeeReportById(UUID reportUuid) {

        return employeeReportRepository
                .findById(reportUuid)
                .orElseThrow(() -> new ResourceNotFoundException("employee.report.notFound"));
    }

    @Transactional(readOnly = true)
    public List<EmployeeReport> getAllEmployeeReports() {

        return employeeReportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EmployeeReport getEmployeeReportByEmployeeUuid(UUID employeeUuid) {
        return employeeReportRepository
                .findEmployeeReportByEmployeeUuid(employeeUuid)
                .orElseThrow(() -> new ResourceNotFoundException("employee.report.notFound"));
    }

    @Transactional
    public EmployeeReport updateEmployeeReport(UUID reportUuid, EmployeeReport employeeReport) {

        EmployeeReport existingEmployeeReport = getEmployeeReportById(reportUuid);
        BeanUtils.copyProperties(employeeReport, existingEmployeeReport, "uuid", "employeeUuid");
        return employeeReportRepository.save(existingEmployeeReport);
    }

    @Transactional
    public UUID deleteEmployeeReportById(UUID reportUuid) {

        QEmployeeReport employeeReport = QEmployeeReport.employeeReport;

        long rowsUpdated = queryFactory
                .update(employeeReport)
                .where(employeeReport.uuid.eq(reportUuid))
                .set(employeeReport.reportStatus, EmployeeReportStatusEnum.INACTIVE)
                .execute();

        if (rowsUpdated == 0) {
            throw new ResourceNotFoundException("employee.report.notFound");
        }

        return reportUuid;
    }
}
