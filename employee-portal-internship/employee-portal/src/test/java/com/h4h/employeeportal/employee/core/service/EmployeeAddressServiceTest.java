package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeAddressStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeAddress;
import com.h4h.employeeportal.employee.core.model.QEmployeeAddress;
import com.h4h.employeeportal.employee.core.repository.EmployeeAddressRepository;
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
class EmployeeAddressServiceTest {

    @Mock
    private EmployeeAddressRepository employeeAddressRepository;

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAUpdateClause updateClause;

    @InjectMocks
    private EmployeeAddressService employeeAddressService;

    private UUID addressUuid;
    private UUID employeeUuid;
    private EmployeeAddress employeeAddress;

    @BeforeEach
    void setUp() {
        addressUuid = UUID.randomUUID();
        employeeUuid = UUID.randomUUID();

        employeeAddress = mockEmployeeAddress(
                addressUuid, employeeUuid, "1000", "Skopje", "Main Street 1", EmployeeAddressStatusEnum.ACTIVE);
    }

    private EmployeeAddress mockEmployeeAddress(
            UUID uuid,
            UUID employeeUuid,
            String postalCode,
            String municipality,
            String street,
            EmployeeAddressStatusEnum status) {

        return EmployeeAddress.builder()
                .uuid(uuid)
                .employeeUuid(employeeUuid)
                .postalCode(postalCode)
                .municipality(municipality)
                .street(street)
                .addressStatus(status)
                .build();
    }

    private void mockDeleteQuery(long affectedRows) {
        when(queryFactory.update(QEmployeeAddress.employeeAddress)).thenReturn(updateClause);

        when(updateClause.where(any(Predicate.class))).thenReturn(updateClause);

        when(updateClause.set(QEmployeeAddress.employeeAddress.addressStatus, EmployeeAddressStatusEnum.INACTIVE))
                .thenReturn(updateClause);

        when(updateClause.execute()).thenReturn(affectedRows);
    }

    private void verifyDeleteQuery() {
        verify(queryFactory).update(QEmployeeAddress.employeeAddress);
        verify(updateClause).where(any(Predicate.class));
        verify(updateClause).set(QEmployeeAddress.employeeAddress.addressStatus, EmployeeAddressStatusEnum.INACTIVE);
        verify(updateClause).execute();
    }

    @Test
    void createEmployeeAddress_shouldSaveAndReturnAddress() {
        when(employeeAddressRepository.save(employeeAddress)).thenReturn(employeeAddress);

        EmployeeAddress result = employeeAddressService.createEmployeeAddress(employeeAddress);

        assertSame(employeeAddress, result);
        verify(employeeAddressRepository).save(employeeAddress);
    }

    @Test
    void createEmployeeAddress_shouldReturnSavedAddressWithGeneratedValues() {
        EmployeeAddress inputAddress = mockEmployeeAddress(null, employeeUuid, "1000", "Skopje", "Main Street 1", null);

        EmployeeAddress savedAddress = mockEmployeeAddress(
                addressUuid, employeeUuid, "1000", "Skopje", "Main Street 1", EmployeeAddressStatusEnum.ACTIVE);

        when(employeeAddressRepository.save(inputAddress)).thenReturn(savedAddress);

        EmployeeAddress result = employeeAddressService.createEmployeeAddress(inputAddress);

        assertSame(savedAddress, result);
        assertEquals(addressUuid, result.getUuid());
        assertEquals(EmployeeAddressStatusEnum.ACTIVE, result.getAddressStatus());

        verify(employeeAddressRepository).save(inputAddress);
    }

    @Test
    void getEmployeeAddressById_shouldReturnAddressWhenFound() {
        when(employeeAddressRepository.findById(addressUuid)).thenReturn(Optional.of(employeeAddress));

        EmployeeAddress result = employeeAddressService.getEmployeeAddressById(addressUuid);

        assertSame(employeeAddress, result);
        verify(employeeAddressRepository).findById(addressUuid);
    }

    @Test
    void getEmployeeAddressById_shouldThrowExceptionWhenAddressNotFound() {
        when(employeeAddressRepository.findById(addressUuid)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeAddressService.getEmployeeAddressById(addressUuid));

        assertEquals("employee.address.notFound", exception.getMessage());
        verify(employeeAddressRepository).findById(addressUuid);
    }

    @Test
    void getAllEmployeeAddresses_shouldReturnAllAddresses() {
        EmployeeAddress secondAddress = mockEmployeeAddress(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "2000",
                "Bitola",
                "Second Street 2",
                EmployeeAddressStatusEnum.ACTIVE);

        List<EmployeeAddress> addresses = List.of(employeeAddress, secondAddress);

        when(employeeAddressRepository.findAll()).thenReturn(addresses);

        List<EmployeeAddress> result = employeeAddressService.getAllEmployeeAddresses();

        assertEquals(addresses, result);
        assertEquals(2, result.size());

        verify(employeeAddressRepository).findAll();
    }

    @Test
    void getAllEmployeeAddresses_shouldReturnEmptyListWhenNoAddressesExist() {
        when(employeeAddressRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeAddress> result = employeeAddressService.getAllEmployeeAddresses();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employeeAddressRepository).findAll();
    }

    @Test
    void getEmployeeAddressByEmployeeUuid_shouldReturnAddressWhenFound() {
        when(employeeAddressRepository.findEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenReturn(Optional.of(employeeAddress));

        EmployeeAddress result = employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid);

        assertSame(employeeAddress, result);

        verify(employeeAddressRepository).findEmployeeAddressByEmployeeUuid(employeeUuid);
    }

    @Test
    void getEmployeeAddressByEmployeeUuid_shouldThrowExceptionWhenAddressNotFound() {
        when(employeeAddressRepository.findEmployeeAddressByEmployeeUuid(employeeUuid))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeAddressService.getEmployeeAddressByEmployeeUuid(employeeUuid));

        assertEquals("employee.address.NotFound", exception.getMessage());

        verify(employeeAddressRepository).findEmployeeAddressByEmployeeUuid(employeeUuid);
    }

    @Test
    void updateEmployeeAddress_shouldUpdatePropertiesAndPreserveIdentifiers() {
        EmployeeAddress updatedAddress = mockEmployeeAddress(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "2000",
                "Bitola",
                "Updated Street 10",
                EmployeeAddressStatusEnum.INACTIVE);

        when(employeeAddressRepository.findById(addressUuid)).thenReturn(Optional.of(employeeAddress));

        when(employeeAddressRepository.save(employeeAddress)).thenReturn(employeeAddress);

        EmployeeAddress result = employeeAddressService.updateEmployeeAddress(addressUuid, updatedAddress);

        assertAll(
                () -> assertSame(employeeAddress, result),
                () -> assertEquals(addressUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals("2000", result.getPostalCode()),
                () -> assertEquals("Bitola", result.getMunicipality()),
                () -> assertEquals("Updated Street 10", result.getStreet()),
                () -> assertEquals(EmployeeAddressStatusEnum.INACTIVE, result.getAddressStatus()));

        verify(employeeAddressRepository).findById(addressUuid);
        verify(employeeAddressRepository).save(employeeAddress);
    }

    @Test
    void updateEmployeeAddress_shouldThrowExceptionWhenAddressNotFound() {
        when(employeeAddressRepository.findById(addressUuid)).thenReturn(Optional.empty());

        EmployeeAddress updatedAddress = mockEmployeeAddress(
                UUID.randomUUID(), employeeUuid, "2000", "Bitola", "Updated Street", EmployeeAddressStatusEnum.ACTIVE);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeAddressService.updateEmployeeAddress(addressUuid, updatedAddress));

        assertEquals("employee.address.notFound", exception.getMessage());

        verify(employeeAddressRepository).findById(addressUuid);
        verify(employeeAddressRepository, never()).save(any());
    }

    @Test
    void deleteEmployeeAddress_shouldReturnUuidWhenAddressExists() {
        mockDeleteQuery(1L);

        UUID result = employeeAddressService.deleteEmployeeAddress(addressUuid);

        assertEquals(addressUuid, result);
        verifyDeleteQuery();
    }

    @Test
    void deleteEmployeeAddress_shouldThrowExceptionWhenNoRowsUpdated() {
        mockDeleteQuery(0L);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeAddressService.deleteEmployeeAddress(addressUuid));

        assertEquals("employee.address.notFound", exception.getMessage());
        verifyDeleteQuery();
    }

    @Test
    void deleteEmployeeAddress_shouldReturnUuidWhenMultipleRowsAreUpdated() {
        mockDeleteQuery(2L);

        UUID result = employeeAddressService.deleteEmployeeAddress(addressUuid);

        assertEquals(addressUuid, result);
        verifyDeleteQuery();
    }
}
