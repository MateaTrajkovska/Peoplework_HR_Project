package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactStatusEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactTypeEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeContact;
import com.h4h.employeeportal.employee.core.model.QEmployeeContact;
import com.h4h.employeeportal.employee.core.repository.EmployeeContactRepository;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeContactServiceTest {

    @Mock
    private EmployeeContactRepository employeeContactRepository;

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAUpdateClause updateClause;

    @InjectMocks
    private EmployeeContactService employeeContactService;

    private UUID contactUuid;
    private UUID employeeUuid;
    private EmployeeContact employeeContact;

    @BeforeEach
    void setUp() {
        contactUuid = UUID.randomUUID();
        employeeUuid = UUID.randomUUID();

        employeeContact = mockEmployeeContact(
                contactUuid,
                employeeUuid,
                "john.doe@example.com",
                "+38970123456",
                EmployeeContactTypeEnum.PERSONAL,
                EmployeeContactStatusEnum.ACTIVE);
    }

    private EmployeeContact mockEmployeeContact(
            UUID uuid,
            UUID employeeUuid,
            String email,
            String phoneNumber,
            EmployeeContactTypeEnum contactType,
            EmployeeContactStatusEnum contactStatus) {

        return EmployeeContact.builder()
                .uuid(uuid)
                .employeeUuid(employeeUuid)
                .email(email)
                .phoneNumber(phoneNumber)
                .contactType(contactType)
                .contactStatus(contactStatus)
                .build();
    }

    private void assertContactEquals(
            EmployeeContact contact,
            UUID uuid,
            UUID employeeUuid,
            String email,
            String phoneNumber,
            EmployeeContactTypeEnum contactType,
            EmployeeContactStatusEnum contactStatus) {

        assertAll(
                () -> assertEquals(uuid, contact.getUuid()),
                () -> assertEquals(employeeUuid, contact.getEmployeeUuid()),
                () -> assertEquals(email, contact.getEmail()),
                () -> assertEquals(phoneNumber, contact.getPhoneNumber()),
                () -> assertEquals(contactType, contact.getContactType()),
                () -> assertEquals(contactStatus, contact.getContactStatus()));
    }

    private void mockDeleteQuery(long affectedRows) {
        when(queryFactory.update(QEmployeeContact.employeeContact)).thenReturn(updateClause);

        when(updateClause.where(any(Predicate.class))).thenReturn(updateClause);

        when(updateClause.set(QEmployeeContact.employeeContact.contactStatus, EmployeeContactStatusEnum.INACTIVE))
                .thenReturn(updateClause);

        when(updateClause.execute()).thenReturn(affectedRows);
    }

    private void verifyDeleteQuery() {
        verify(queryFactory).update(QEmployeeContact.employeeContact);
        verify(updateClause).where(any(Predicate.class));
        verify(updateClause).set(QEmployeeContact.employeeContact.contactStatus, EmployeeContactStatusEnum.INACTIVE);
        verify(updateClause).execute();
    }

    @Test
    void createEmployeeContact_shouldSaveAndReturnContact() {
        when(employeeContactRepository.save(employeeContact)).thenReturn(employeeContact);

        EmployeeContact result = employeeContactService.createEmployeeContact(employeeContact);

        assertSame(employeeContact, result);
        verify(employeeContactRepository).save(employeeContact);
    }

    @Test
    void createEmployeeContact_shouldReturnSavedContactWithGeneratedValues() {
        EmployeeContact inputContact = mockEmployeeContact(
                null, employeeUuid, "john.doe@company.com", "+38970123456", EmployeeContactTypeEnum.CORPORATE, null);

        EmployeeContact savedContact = mockEmployeeContact(
                contactUuid,
                employeeUuid,
                "john.doe@company.com",
                "+38970123456",
                EmployeeContactTypeEnum.CORPORATE,
                EmployeeContactStatusEnum.ACTIVE);

        when(employeeContactRepository.save(inputContact)).thenReturn(savedContact);

        EmployeeContact result = employeeContactService.createEmployeeContact(inputContact);

        assertSame(savedContact, result);

        assertContactEquals(
                result,
                contactUuid,
                employeeUuid,
                "john.doe@company.com",
                "+38970123456",
                EmployeeContactTypeEnum.CORPORATE,
                EmployeeContactStatusEnum.ACTIVE);

        verify(employeeContactRepository).save(inputContact);
    }

    @Test
    void getEmployeeContactById_shouldReturnContactWhenFound() {
        when(employeeContactRepository.findById(contactUuid)).thenReturn(Optional.of(employeeContact));

        EmployeeContact result = employeeContactService.getEmployeeContactById(contactUuid);

        assertSame(employeeContact, result);
        verify(employeeContactRepository).findById(contactUuid);
    }

    @Test
    void getEmployeeContactById_shouldThrowExceptionWhenContactNotFound() {
        when(employeeContactRepository.findById(contactUuid)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeContactService.getEmployeeContactById(contactUuid));

        assertEquals("employee.contact.notFound", exception.getMessage());
        verify(employeeContactRepository).findById(contactUuid);
    }

    @Test
    void getAllEmployeeContacts_shouldReturnAllContacts() {
        EmployeeContact secondContact = mockEmployeeContact(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "jane.doe@example.com",
                "+38971234567",
                EmployeeContactTypeEnum.CORPORATE,
                EmployeeContactStatusEnum.ACTIVE);

        List<EmployeeContact> contacts = List.of(employeeContact, secondContact);

        when(employeeContactRepository.findAll()).thenReturn(contacts);

        List<EmployeeContact> result = employeeContactService.getAllEmployeeContacts();

        assertEquals(contacts, result);
        assertEquals(2, result.size());

        verify(employeeContactRepository).findAll();
    }

    @Test
    void getAllEmployeeContacts_shouldReturnEmptyListWhenNoContactsExist() {
        when(employeeContactRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeContact> result = employeeContactService.getAllEmployeeContacts();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employeeContactRepository).findAll();
    }

    @Test
    void getEmployeeContactsByEmployeeUuid_shouldReturnEmployeeContacts() {
        EmployeeContact secondContact = mockEmployeeContact(
                UUID.randomUUID(),
                employeeUuid,
                "john.doe@company.com",
                "+38971987654",
                EmployeeContactTypeEnum.CORPORATE,
                EmployeeContactStatusEnum.ACTIVE);

        List<EmployeeContact> contacts = List.of(employeeContact, secondContact);

        when(employeeContactRepository.findEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenReturn(contacts);

        List<EmployeeContact> result = employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid);

        assertEquals(contacts, result);
        assertEquals(2, result.size());
        assertEquals(employeeUuid, result.getFirst().getEmployeeUuid());
        assertEquals(employeeUuid, result.get(1).getEmployeeUuid());

        verify(employeeContactRepository).findEmployeeContactsByEmployeeUuid(employeeUuid);
    }

    @Test
    void getEmployeeContactsByEmployeeUuid_shouldReturnEmptyListWhenEmployeeHasNoContacts() {
        when(employeeContactRepository.findEmployeeContactsByEmployeeUuid(employeeUuid))
                .thenReturn(Collections.emptyList());

        List<EmployeeContact> result = employeeContactService.getEmployeeContactsByEmployeeUuid(employeeUuid);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employeeContactRepository).findEmployeeContactsByEmployeeUuid(employeeUuid);
    }

    @Test
    void updateEmployeeContact_shouldUpdatePropertiesAndPreserveIdentifiers() {
        EmployeeContact updatedContact = mockEmployeeContact(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "updated@example.com",
                "+38970999888",
                EmployeeContactTypeEnum.CORPORATE,
                EmployeeContactStatusEnum.INACTIVE);

        when(employeeContactRepository.findById(contactUuid)).thenReturn(Optional.of(employeeContact));

        when(employeeContactRepository.save(employeeContact)).thenReturn(employeeContact);

        EmployeeContact result = employeeContactService.updateEmployeeContact(contactUuid, updatedContact);

        assertSame(employeeContact, result);

        assertContactEquals(
                result,
                contactUuid,
                employeeUuid,
                "updated@example.com",
                "+38970999888",
                EmployeeContactTypeEnum.CORPORATE,
                EmployeeContactStatusEnum.INACTIVE);

        verify(employeeContactRepository).findById(contactUuid);
        verify(employeeContactRepository).save(employeeContact);
    }

    @Test
    void updateEmployeeContact_shouldThrowExceptionWhenContactNotFound() {
        when(employeeContactRepository.findById(contactUuid)).thenReturn(Optional.empty());

        EmployeeContact updatedContact = mockEmployeeContact(
                UUID.randomUUID(),
                employeeUuid,
                "updated@example.com",
                "+38970999888",
                EmployeeContactTypeEnum.CORPORATE,
                EmployeeContactStatusEnum.ACTIVE);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeContactService.updateEmployeeContact(contactUuid, updatedContact));

        assertEquals("employee.contact.notFound", exception.getMessage());

        verify(employeeContactRepository).findById(contactUuid);
        verify(employeeContactRepository, never()).save(any());
    }

    @Test
    void deleteEmployeeContact_shouldReturnUuidWhenContactExists() {
        mockDeleteQuery(1L);

        UUID result = employeeContactService.deleteEmployeeContact(contactUuid);

        assertEquals(contactUuid, result);
        verifyDeleteQuery();
    }

    @Test
    void deleteEmployeeContact_shouldThrowExceptionWhenNoRowsUpdated() {
        mockDeleteQuery(0L);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeContactService.deleteEmployeeContact(contactUuid));

        assertEquals("employee.contact.notFound", exception.getMessage());
        verifyDeleteQuery();
    }

    @Test
    void deleteEmployeeContact_shouldReturnUuidWhenMultipleRowsAreUpdated() {
        mockDeleteQuery(2L);

        UUID result = employeeContactService.deleteEmployeeContact(contactUuid);

        assertEquals(contactUuid, result);
        verifyDeleteQuery();
    }
}
