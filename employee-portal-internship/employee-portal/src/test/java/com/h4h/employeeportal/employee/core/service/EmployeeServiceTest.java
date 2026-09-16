package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeGenderEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeStatusEnum;
import com.h4h.employeeportal.employee.core.model.Employee;
import com.h4h.employeeportal.employee.core.model.QEmployee;
import com.h4h.employeeportal.employee.core.repository.EmployeeRepository;
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
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAUpdateClause updateClause;

    @InjectMocks
    private EmployeeService employeeService;

    private UUID employeeUuid;
    private UUID userUuid;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeUuid = UUID.randomUUID();
        userUuid = UUID.randomUUID();

        employee = createEmployee(
                employeeUuid,
                "John",
                "Doe",
                "123456789",
                EmployeeStatusEnum.ACTIVE,
                10000L,
                EmployeeGenderEnum.MALE,
                "Macedonian",
                userUuid);
    }

    private Employee createEmployee(
            UUID uuid,
            String firstName,
            String lastName,
            String personalId,
            EmployeeStatusEnum status,
            Long employeeNumber,
            EmployeeGenderEnum gender,
            String nationality,
            UUID userUuid) {

        return Employee.builder()
                .uuid(uuid)
                .firstName(firstName)
                .lastName(lastName)
                .personalId(personalId)
                .status(status)
                .employeeNumber(employeeNumber)
                .gender(gender)
                .nationality(nationality)
                .userUuid(userUuid)
                .build();
    }

    private void mockDeleteQuery(long affectedRows) {
        when(queryFactory.update(QEmployee.employee)).thenReturn(updateClause);

        when(updateClause.where(any(Predicate.class))).thenReturn(updateClause);

        when(updateClause.set(QEmployee.employee.status, EmployeeStatusEnum.ARCHIVED))
                .thenReturn(updateClause);

        when(updateClause.execute()).thenReturn(affectedRows);
    }

    private void verifyDeleteQuery() {
        verify(queryFactory).update(QEmployee.employee);
        verify(updateClause).where(any(Predicate.class));
        verify(updateClause).set(QEmployee.employee.status, EmployeeStatusEnum.ARCHIVED);
        verify(updateClause).execute();
    }

    @Test
    void createEmployee_shouldSaveAndReturnEmployeeWithAllFields() {
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.createEmployee(employee);

        assertAll(
                () -> assertSame(employee, result),
                () -> assertEquals(employeeUuid, result.getUuid()),
                () -> assertEquals("John", result.getFirstName()),
                () -> assertEquals("Doe", result.getLastName()),
                () -> assertEquals("123456789", result.getPersonalId()),
                () -> assertEquals(EmployeeStatusEnum.ACTIVE, result.getStatus()),
                () -> assertEquals(10000L, result.getEmployeeNumber()),
                () -> assertEquals(EmployeeGenderEnum.MALE, result.getGender()),
                () -> assertEquals("Macedonian", result.getNationality()),
                () -> assertEquals(userUuid, result.getUserUuid()));

        verify(employeeRepository).save(employee);
    }

    @Test
    void createEmployee_shouldSaveAndReturnEmployeeWithoutOptionalFields() {
        Employee employeeWithoutOptionalFields =
                createEmployee(null, "Jane", "Smith", null, null, null, null, "Macedonian", null);

        Employee savedEmployee = createEmployee(
                employeeUuid, "Jane", "Smith", null, EmployeeStatusEnum.ACTIVE, 10000L, null, "Macedonian", null);

        when(employeeRepository.save(employeeWithoutOptionalFields)).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(employeeWithoutOptionalFields);

        assertAll(
                () -> assertSame(savedEmployee, result),
                () -> assertEquals(employeeUuid, result.getUuid()),
                () -> assertEquals("Jane", result.getFirstName()),
                () -> assertEquals("Smith", result.getLastName()),
                () -> assertNull(result.getPersonalId()),
                () -> assertEquals(EmployeeStatusEnum.ACTIVE, result.getStatus()),
                () -> assertEquals(10000L, result.getEmployeeNumber()),
                () -> assertNull(result.getGender()),
                () -> assertEquals("Macedonian", result.getNationality()),
                () -> assertNull(result.getUserUuid()));

        verify(employeeRepository).save(employeeWithoutOptionalFields);
    }

    @Test
    void createEmployee_shouldReturnGeneratedEmployeeNumber() {
        Employee newEmployee =
                createEmployee(null, "John", "Doe", null, null, null, EmployeeGenderEnum.MALE, "Macedonian", null);

        Employee savedEmployee = createEmployee(
                employeeUuid,
                "John",
                "Doe",
                null,
                EmployeeStatusEnum.ACTIVE,
                10000L,
                EmployeeGenderEnum.MALE,
                "Macedonian",
                null);

        when(employeeRepository.save(newEmployee)).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(newEmployee);

        assertAll(
                () -> assertNotNull(result.getEmployeeNumber()),
                () -> assertEquals(10000L, result.getEmployeeNumber()));

        verify(employeeRepository).save(newEmployee);
    }

    @Test
    void createEmployee_shouldReturnNextGeneratedEmployeeNumber() {
        Employee newEmployee =
                createEmployee(null, "Jane", "Doe", null, null, null, EmployeeGenderEnum.FEMALE, "Macedonian", null);

        Employee savedEmployee = createEmployee(
                employeeUuid,
                "Jane",
                "Doe",
                null,
                EmployeeStatusEnum.ACTIVE,
                10001L,
                EmployeeGenderEnum.FEMALE,
                "Macedonian",
                null);

        when(employeeRepository.save(newEmployee)).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(newEmployee);

        assertAll(
                () -> assertNotNull(result.getEmployeeNumber()),
                () -> assertEquals(10001L, result.getEmployeeNumber()));

        verify(employeeRepository).save(newEmployee);
    }

    @Test
    void getEmployeeById_shouldReturnEmployeeWhenFound() {
        when(employeeRepository.findById(employeeUuid)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(employeeUuid);

        assertSame(employee, result);

        verify(employeeRepository).findById(employeeUuid);
    }

    @Test
    void getEmployeeById_shouldThrowExceptionWhenEmployeeNotFound() {
        when(employeeRepository.findById(employeeUuid)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(employeeUuid));

        assertEquals("employee.notFound", exception.getMessage());

        verify(employeeRepository).findById(employeeUuid);
    }

    @Test
    void getAllEmployees_shouldReturnAllEmployees() {
        Employee secondEmployee = createEmployee(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "987654321",
                EmployeeStatusEnum.ACTIVE,
                10001L,
                EmployeeGenderEnum.FEMALE,
                "Macedonian",
                UUID.randomUUID());

        List<Employee> employees = List.of(employee, secondEmployee);

        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertAll(() -> assertEquals(employees, result), () -> assertEquals(2, result.size()));

        verify(employeeRepository).findAll();
    }

    @Test
    void getAllEmployees_shouldReturnEmptyListWhenNoEmployeesExist() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Employee> result = employeeService.getAllEmployees();

        assertAll(() -> assertNotNull(result), () -> assertTrue(result.isEmpty()));

        verify(employeeRepository).findAll();
    }

    @Test
    void updateEmployee_shouldUpdateFieldsAndPreserveUuidAndEmployeeNumber() {
        UUID originalUuid = employee.getUuid();
        Long originalEmployeeNumber = employee.getEmployeeNumber();

        Employee updatedEmployee = createEmployee(
                UUID.randomUUID(),
                "UpdatedJohn",
                "UpdatedDoe",
                "987654321",
                EmployeeStatusEnum.ARCHIVED,
                99999L,
                EmployeeGenderEnum.OTHER,
                "German",
                UUID.randomUUID());

        when(employeeRepository.findById(originalUuid)).thenReturn(Optional.of(employee));

        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.updateEmployee(originalUuid, updatedEmployee);

        assertAll(
                () -> assertSame(employee, result),
                () -> assertEquals(originalUuid, result.getUuid()),
                () -> assertEquals(originalEmployeeNumber, result.getEmployeeNumber()),
                () -> assertEquals("UpdatedJohn", result.getFirstName()),
                () -> assertEquals("UpdatedDoe", result.getLastName()),
                () -> assertEquals("987654321", result.getPersonalId()),
                () -> assertEquals(EmployeeStatusEnum.ARCHIVED, result.getStatus()),
                () -> assertEquals(EmployeeGenderEnum.OTHER, result.getGender()),
                () -> assertEquals("German", result.getNationality()),
                () -> assertEquals(updatedEmployee.getUserUuid(), result.getUserUuid()));

        verify(employeeRepository).findById(originalUuid);
        verify(employeeRepository).save(employee);
    }

    @Test
    void updateEmployee_shouldUpdateEmployeeWithoutOptionalFields() {
        Employee updatedEmployee = createEmployee(
                UUID.randomUUID(),
                "UpdatedJohn",
                "UpdatedDoe",
                null,
                EmployeeStatusEnum.ACTIVE,
                99999L,
                null,
                "Macedonian",
                null);

        when(employeeRepository.findById(employeeUuid)).thenReturn(Optional.of(employee));

        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.updateEmployee(employeeUuid, updatedEmployee);

        assertAll(
                () -> assertEquals(employeeUuid, result.getUuid()),
                () -> assertEquals(10000L, result.getEmployeeNumber()),
                () -> assertEquals("UpdatedJohn", result.getFirstName()),
                () -> assertEquals("UpdatedDoe", result.getLastName()),
                () -> assertNull(result.getPersonalId()),
                () -> assertEquals(EmployeeStatusEnum.ACTIVE, result.getStatus()),
                () -> assertNull(result.getGender()),
                () -> assertEquals("Macedonian", result.getNationality()),
                () -> assertNull(result.getUserUuid()));

        verify(employeeRepository).findById(employeeUuid);
        verify(employeeRepository).save(employee);
    }

    @Test
    void updateEmployee_shouldPreserveEmployeeNumberWhenUpdatedNumberIsProvided() {
        Employee updatedEmployee = createEmployee(
                UUID.randomUUID(),
                "UpdatedJohn",
                "UpdatedDoe",
                "987654321",
                EmployeeStatusEnum.ACTIVE,
                50000L,
                EmployeeGenderEnum.MALE,
                "Macedonian",
                userUuid);

        when(employeeRepository.findById(employeeUuid)).thenReturn(Optional.of(employee));

        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.updateEmployee(employeeUuid, updatedEmployee);

        assertAll(
                () -> assertEquals(10000L, result.getEmployeeNumber()),
                () -> assertNotEquals(updatedEmployee.getEmployeeNumber(), result.getEmployeeNumber()));

        verify(employeeRepository).findById(employeeUuid);
        verify(employeeRepository).save(employee);
    }

    @Test
    void updateEmployee_shouldThrowExceptionWhenEmployeeNotFound() {
        when(employeeRepository.findById(employeeUuid)).thenReturn(Optional.empty());

        Employee updatedEmployee = createEmployee(
                UUID.randomUUID(),
                "UpdatedJohn",
                "UpdatedDoe",
                null,
                EmployeeStatusEnum.ACTIVE,
                99999L,
                EmployeeGenderEnum.MALE,
                "Macedonian",
                null);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeService.updateEmployee(employeeUuid, updatedEmployee));

        assertEquals("employee.notFound", exception.getMessage());

        verify(employeeRepository).findById(employeeUuid);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deleteEmployee_shouldReturnUuidWhenEmployeeExists() {
        mockDeleteQuery(1L);

        UUID result = employeeService.deleteEmployee(employeeUuid);

        assertEquals(employeeUuid, result);

        verifyDeleteQuery();
    }

    @Test
    void deleteEmployee_shouldThrowExceptionWhenNoRowsUpdated() {
        mockDeleteQuery(0L);

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(employeeUuid));

        assertEquals("employee.notFound", exception.getMessage());

        verifyDeleteQuery();
    }

    @Test
    void deleteEmployee_shouldReturnUuidWhenMultipleRowsAreUpdated() {
        mockDeleteQuery(2L);

        UUID result = employeeService.deleteEmployee(employeeUuid);

        assertEquals(employeeUuid, result);

        verifyDeleteQuery();
    }
}
