package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeAddressStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeAddress;
import com.h4h.employeeportal.employee.core.model.QEmployeeAddress;
import com.h4h.employeeportal.employee.core.repository.EmployeeAddressRepository;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class EmployeeAddressService {

    private final EmployeeAddressRepository employeeAddressRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public EmployeeAddress createEmployeeAddress(EmployeeAddress employeeAddress) {

        return employeeAddressRepository.save(employeeAddress);
    }

    @Transactional(readOnly = true)
    public EmployeeAddress getEmployeeAddressById(UUID addressUuid) {

        return employeeAddressRepository
                .findById(addressUuid)
                .orElseThrow(() -> new ResourceNotFoundException("employee.address.notFound"));
    }

    @Transactional(readOnly = true)
    public List<EmployeeAddress> getAllEmployeeAddresses() {
        return employeeAddressRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EmployeeAddress getEmployeeAddressByEmployeeUuid(UUID employeeUuid) {
        return employeeAddressRepository
                .findEmployeeAddressByEmployeeUuid(employeeUuid)
                .orElseThrow(() -> new ResourceNotFoundException("employee.address.NotFound"));
    }

    @Transactional
    public EmployeeAddress updateEmployeeAddress(UUID addressUuid, EmployeeAddress employeeAddress) {
        EmployeeAddress existingEmployeeAddress = getEmployeeAddressById(addressUuid);
        BeanUtils.copyProperties(employeeAddress, existingEmployeeAddress, "uuid", "employeeUuid");
        return employeeAddressRepository.save(existingEmployeeAddress);
    }

    @Transactional
    public UUID deleteEmployeeAddress(UUID addressUuid) {
        QEmployeeAddress employeeAddress = QEmployeeAddress.employeeAddress;

        long rowsUpdated = queryFactory
                .update(employeeAddress)
                .where(employeeAddress.uuid.eq(addressUuid))
                .set(employeeAddress.addressStatus, EmployeeAddressStatusEnum.INACTIVE)
                .execute();
        if (rowsUpdated == 0) {
            throw new ResourceNotFoundException("employee.address.notFound");
        }

        return addressUuid;
    }
}
