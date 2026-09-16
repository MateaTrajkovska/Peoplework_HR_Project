package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.BankStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeFinance;
import com.h4h.employeeportal.employee.core.model.QEmployeeFinance;
import com.h4h.employeeportal.employee.core.repository.EmployeeFinanceRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeFinanceServiceTest {

    @Mock
    private EmployeeFinanceRepository employeeFinanceRepository;

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAUpdateClause updateClause;

    @InjectMocks
    private EmployeeFinanceService employeeFinanceService;

    private UUID financeUuid;
    private UUID employeeUuid;
    private EmployeeFinance employeeFinance;

    @BeforeEach
    void setUp() {
        financeUuid = UUID.randomUUID();
        employeeUuid = UUID.randomUUID();

        employeeFinance = mockEmployeeFinance(financeUuid, employeeUuid, 123456789L, BankStatusEnum.ACTIVE);
    }

    private EmployeeFinance mockEmployeeFinance(
            UUID uuid, UUID employeeUuid, Long bankAccount, BankStatusEnum bankStatus) {

        return EmployeeFinance.builder()
                .uuid(uuid)
                .employeeUuid(employeeUuid)
                .bankAccount(bankAccount)
                .bankStatus(bankStatus)
                .build();
    }

    private void mockDeleteQuery(long updatedRows) {
        when(queryFactory.update(QEmployeeFinance.employeeFinance)).thenReturn(updateClause);

        when(updateClause.where(any(Predicate.class))).thenReturn(updateClause);

        when(updateClause.set(QEmployeeFinance.employeeFinance.bankStatus, BankStatusEnum.INACTIVE))
                .thenReturn(updateClause);

        when(updateClause.execute()).thenReturn(updatedRows);
    }

    private void verifyDeleteQuery() {
        verify(queryFactory).update(QEmployeeFinance.employeeFinance);
        verify(updateClause).where(any(Predicate.class));
        verify(updateClause).set(QEmployeeFinance.employeeFinance.bankStatus, BankStatusEnum.INACTIVE);
        verify(updateClause).execute();
    }

    @Test
    void createEmployeeFinance_shouldSaveAndReturnFinance() {
        when(employeeFinanceRepository.save(employeeFinance)).thenReturn(employeeFinance);

        EmployeeFinance result = employeeFinanceService.createEmployeeFinance(employeeFinance);

        assertSame(employeeFinance, result);
        verify(employeeFinanceRepository).save(employeeFinance);
    }

    @Test
    void createEmployeeFinance_shouldReturnSavedFinanceWithGeneratedValues() {
        EmployeeFinance inputFinance = mockEmployeeFinance(null, employeeUuid, 987654321L, null);

        EmployeeFinance savedFinance =
                mockEmployeeFinance(financeUuid, employeeUuid, 987654321L, BankStatusEnum.ACTIVE);

        when(employeeFinanceRepository.save(inputFinance)).thenReturn(savedFinance);

        EmployeeFinance result = employeeFinanceService.createEmployeeFinance(inputFinance);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(financeUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals(987654321L, result.getBankAccount()),
                () -> assertEquals(BankStatusEnum.ACTIVE, result.getBankStatus()));

        verify(employeeFinanceRepository).save(inputFinance);
    }

    @Test
    void getEmployeeFinanceById_shouldReturnFinanceWhenFound() {
        when(employeeFinanceRepository.findById(financeUuid)).thenReturn(Optional.of(employeeFinance));

        EmployeeFinance result = employeeFinanceService.getEmployeeFinanceById(financeUuid);

        assertSame(employeeFinance, result);
        verify(employeeFinanceRepository).findById(financeUuid);
    }

    @Test
    void getEmployeeFinanceById_shouldThrowExceptionWhenFinanceNotFound() {
        when(employeeFinanceRepository.findById(financeUuid)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeFinanceService.getEmployeeFinanceById(financeUuid));

        assertEquals("employee.finance.notFound", exception.getMessage());
        verify(employeeFinanceRepository).findById(financeUuid);
    }

    @Test
    void getAllEmployeeFinances_shouldReturnAllFinances() {
        EmployeeFinance secondFinance =
                mockEmployeeFinance(UUID.randomUUID(), UUID.randomUUID(), 987654321L, BankStatusEnum.ACTIVE);

        List<EmployeeFinance> finances = List.of(employeeFinance, secondFinance);

        when(employeeFinanceRepository.findAll()).thenReturn(finances);

        List<EmployeeFinance> result = employeeFinanceService.getAllEmployeeFinances();

        assertEquals(finances, result);
        assertEquals(2, result.size());

        verify(employeeFinanceRepository).findAll();
    }

    @Test
    void getAllEmployeeFinances_shouldReturnEmptyListWhenNoFinancesExist() {
        when(employeeFinanceRepository.findAll()).thenReturn(List.of());

        List<EmployeeFinance> result = employeeFinanceService.getAllEmployeeFinances();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employeeFinanceRepository).findAll();
    }

    @Test
    void getEmployeeFinanceByEmployeeUuid_shouldReturnFinanceWhenFound() {
        when(employeeFinanceRepository.findEmployeeByEmployeeUuid(employeeUuid))
                .thenReturn(Optional.of(employeeFinance));

        EmployeeFinance result = employeeFinanceService.getEmployeeFinanceByEmployeeUuid(employeeUuid);

        assertSame(employeeFinance, result);

        verify(employeeFinanceRepository).findEmployeeByEmployeeUuid(employeeUuid);
    }

    @Test
    void getEmployeeFinanceByEmployeeUuid_shouldThrowExceptionWhenFinanceNotFound() {
        when(employeeFinanceRepository.findEmployeeByEmployeeUuid(employeeUuid)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeFinanceService.getEmployeeFinanceByEmployeeUuid(employeeUuid));

        assertEquals("employee.finance.notFound", exception.getMessage());

        verify(employeeFinanceRepository).findEmployeeByEmployeeUuid(employeeUuid);
    }

    @Test
    void updateEmployeeFinance_shouldUpdateBankAccountAndPreserveIdentifiers() {
        EmployeeFinance updatedFinance =
                mockEmployeeFinance(UUID.randomUUID(), UUID.randomUUID(), 987654321L, BankStatusEnum.INACTIVE);

        when(employeeFinanceRepository.findById(financeUuid)).thenReturn(Optional.of(employeeFinance));

        when(employeeFinanceRepository.save(employeeFinance)).thenReturn(employeeFinance);

        EmployeeFinance result = employeeFinanceService.updateEmployeeFinance(financeUuid, updatedFinance);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(financeUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals(987654321L, result.getBankAccount()),
                () -> assertEquals(BankStatusEnum.INACTIVE, result.getBankStatus()));

        verify(employeeFinanceRepository).findById(financeUuid);
        verify(employeeFinanceRepository).save(employeeFinance);
    }

    @Test
    void updateEmployeeFinance_shouldThrowExceptionWhenFinanceNotFound() {
        when(employeeFinanceRepository.findById(financeUuid)).thenReturn(Optional.empty());

        EmployeeFinance updatedFinance =
                mockEmployeeFinance(UUID.randomUUID(), employeeUuid, 987654321L, BankStatusEnum.ACTIVE);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeFinanceService.updateEmployeeFinance(financeUuid, updatedFinance));

        assertEquals("employee.finance.notFound", exception.getMessage());

        verify(employeeFinanceRepository).findById(financeUuid);
        verify(employeeFinanceRepository, never()).save(any());
    }

    @Test
    void deleteEmployeeFinance_shouldReturnUuidWhenFinanceExists() {
        mockDeleteQuery(1L);

        UUID result = employeeFinanceService.deleteEmployeeFinance(financeUuid);

        assertEquals(financeUuid, result);
        verifyDeleteQuery();
    }

    @Test
    void deleteEmployeeFinance_shouldThrowExceptionWhenNoRowsUpdated() {
        mockDeleteQuery(0L);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeFinanceService.deleteEmployeeFinance(financeUuid));

        assertEquals("employee.finance.notFound", exception.getMessage());
        verifyDeleteQuery();
    }

    @Test
    void deleteEmployeeFinance_shouldReturnUuidWhenMultipleRowsAreUpdated() {
        mockDeleteQuery(2L);

        UUID result = employeeFinanceService.deleteEmployeeFinance(financeUuid);

        assertEquals(financeUuid, result);
        verifyDeleteQuery();
    }
}
