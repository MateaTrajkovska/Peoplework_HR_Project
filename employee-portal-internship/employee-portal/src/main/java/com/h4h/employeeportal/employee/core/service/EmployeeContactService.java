package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeContact;
import com.h4h.employeeportal.employee.core.model.QEmployeeContact;
import com.h4h.employeeportal.employee.core.repository.EmployeeContactRepository;
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
public class EmployeeContactService {

    private final EmployeeContactRepository employeeContactRepository;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public EmployeeContact createEmployeeContact(EmployeeContact employeeContact) {
        return employeeContactRepository.save(employeeContact);
    }

    @Transactional(readOnly = true)
    public EmployeeContact getEmployeeContactById(UUID employeeContactUuid) {
        return employeeContactRepository
                .findById(employeeContactUuid)
                .orElseThrow(() -> new ResourceNotFoundException("employee.contact.notFound"));
    }

    @Transactional(readOnly = true)
    public List<EmployeeContact> getAllEmployeeContacts() {
        return employeeContactRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<EmployeeContact> getEmployeeContactsByEmployeeUuid(UUID employeeUuid) {
        return employeeContactRepository.findEmployeeContactsByEmployeeUuid(employeeUuid);
    }

    @Transactional
    public EmployeeContact updateEmployeeContact(UUID employeeContactUuid, EmployeeContact updatedEmployeeContact) {
        EmployeeContact existingEmployeeContact = getEmployeeContactById(employeeContactUuid);
        BeanUtils.copyProperties(updatedEmployeeContact, existingEmployeeContact, "uuid", "employeeUuid");
        return employeeContactRepository.save(existingEmployeeContact);
    }

    @Transactional
    public UUID deleteEmployeeContact(UUID employeeContactUuid) {
        QEmployeeContact employeeContact = QEmployeeContact.employeeContact;

        long rowsUpdated = queryFactory
                .update(employeeContact)
                .where(employeeContact.uuid.eq(employeeContactUuid))
                .set(employeeContact.contactStatus, EmployeeContactStatusEnum.INACTIVE)
                .execute();

        if (rowsUpdated == 0) {
            throw new ResourceNotFoundException("employee.contact.notFound");
        }
        return employeeContactUuid;
    }
}
