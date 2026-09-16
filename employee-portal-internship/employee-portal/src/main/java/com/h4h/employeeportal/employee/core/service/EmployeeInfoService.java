package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeInfoStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeInfo;
import com.h4h.employeeportal.employee.core.model.QEmployeeInfo;
import com.h4h.employeeportal.employee.core.repository.EmployeeInfoRepository;
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
public class EmployeeInfoService {

    private final EmployeeInfoRepository employeeInfoRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public EmployeeInfo createEmployeeInfo(EmployeeInfo employeeInfo) {

        return employeeInfoRepository.save(employeeInfo);
    }

    @Transactional(readOnly = true)
    public EmployeeInfo getEmployeeInfoById(UUID infoUuid) {

        return employeeInfoRepository
                .findById(infoUuid)
                .orElseThrow(() -> new ResourceNotFoundException("employee.info.notFound"));
    }

    @Transactional(readOnly = true)
    public List<EmployeeInfo> getAllEmployeeInfo() {
        return employeeInfoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EmployeeInfo getEmployeeInfoByEmployeeUuid(UUID employeeUuid) {
        return employeeInfoRepository
                .findEmployeeInfoByEmployeeUuid(employeeUuid)
                .orElseThrow(() -> new ResourceNotFoundException("employee.info.notFound"));
    }

    @Transactional
    public EmployeeInfo updateEmployeeInfo(UUID infoUuid, EmployeeInfo employeeInfo) {
        EmployeeInfo existinfEmployeeInfo = getEmployeeInfoById(infoUuid);
        BeanUtils.copyProperties(employeeInfo, existinfEmployeeInfo, "uuid", "employeeUuid");
        return employeeInfoRepository.save(existinfEmployeeInfo);
    }

    @Transactional
    public UUID deleteEmployeeInfoById(UUID infoUuid) {
        QEmployeeInfo employeeInfo = QEmployeeInfo.employeeInfo;

        long rowsUpdated = queryFactory
                .update(employeeInfo)
                .where(employeeInfo.uuid.eq(infoUuid))
                .set(employeeInfo.infoStatus, EmployeeInfoStatusEnum.INACTIVE)
                .execute();

        if (rowsUpdated == 0) {
            throw new ResourceNotFoundException("employee.info.notFound");
        }

        return infoUuid;
    }
}
