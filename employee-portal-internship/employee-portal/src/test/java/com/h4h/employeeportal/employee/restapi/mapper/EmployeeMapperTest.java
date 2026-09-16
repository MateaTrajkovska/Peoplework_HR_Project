package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeGenderEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeStatusEnum;
import com.h4h.employeeportal.employee.core.model.Employee;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeDetailsDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeListDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    private EmployeeMapper employeeMapper;

    private UUID employeeUuid;
    private UUID userUuid;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeMapper = new EmployeeMapperImpl();

        employeeUuid = UUID.randomUUID();
        userUuid = UUID.randomUUID();

        employee = mockEmployee(
                employeeUuid,
                userUuid,
                "John",
                "Doe",
                "123456789",
                EmployeeStatusEnum.ACTIVE,
                1000L,
                EmployeeGenderEnum.MALE,
                "Macedonian");
    }

    @Test
    void toDto_shouldMapAllFields() {
        EmployeeDto result = employeeMapper.toDto(employee);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(employeeUuid, result.getId()),
                () -> assertEquals(userUuid, result.getUserId()),
                () -> assertEquals("John", result.getFirstName()),
                () -> assertEquals("Doe", result.getLastName()),
                () -> assertEquals("123456789", result.getPersonalId()),
                () -> assertEquals(EmployeeStatusEnum.ACTIVE, result.getStatus()),
                () -> assertEquals(1000L, result.getEmployeeNumber()),
                () -> assertEquals(EmployeeGenderEnum.MALE, result.getGender()),
                () -> assertEquals("Macedonian", result.getNationality()));
    }

    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        assertNull(employeeMapper.toDto(null));
    }

    @Test
    void toDto_shouldMapNullValues() {
        Employee result = mockEmployee(employeeUuid, null, null, null, null, null, null, null, null);

        EmployeeDto dto = employeeMapper.toDto(result);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(employeeUuid, dto.getId()),
                () -> assertNull(dto.getUserId()),
                () -> assertNull(dto.getFirstName()),
                () -> assertNull(dto.getLastName()),
                () -> assertNull(dto.getPersonalId()),
                () -> assertNull(dto.getStatus()),
                () -> assertNull(dto.getEmployeeNumber()),
                () -> assertNull(dto.getGender()),
                () -> assertNull(dto.getNationality()));
    }

    @Test
    void toEntity_shouldMapAllFields() {
        EmployeeDto dto = mockEmployeeDto(
                employeeUuid,
                userUuid,
                "Jane",
                "Smith",
                "987654321",
                EmployeeStatusEnum.ARCHIVED,
                2000L,
                EmployeeGenderEnum.FEMALE,
                "Macedonian");

        Employee result = employeeMapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(employeeUuid, result.getUuid()),
                () -> assertEquals(userUuid, result.getUserUuid()),
                () -> assertEquals("Jane", result.getFirstName()),
                () -> assertEquals("Smith", result.getLastName()),
                () -> assertEquals("987654321", result.getPersonalId()),
                () -> assertEquals(EmployeeStatusEnum.ARCHIVED, result.getStatus()),
                () -> assertEquals(2000L, result.getEmployeeNumber()),
                () -> assertEquals(EmployeeGenderEnum.FEMALE, result.getGender()),
                () -> assertEquals("Macedonian", result.getNationality()));
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeMapper.toEntity(null));
    }

    @Test
    void toEntity_shouldMapNullValues() {
        Employee result =
                employeeMapper.toEntity(mockEmployeeDto(null, null, null, null, null, null, null, null, null));

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getUserUuid()),
                () -> assertNull(result.getFirstName()),
                () -> assertNull(result.getLastName()),
                () -> assertNull(result.getPersonalId()),
                () -> assertNull(result.getStatus()),
                () -> assertNull(result.getEmployeeNumber()),
                () -> assertNull(result.getGender()),
                () -> assertNull(result.getNationality()));
    }

    @Test
    void toDtoDetails_shouldMapFieldsAndIgnoreRelatedData() {
        EmployeeDetailsDto result = employeeMapper.toDtoDetails(employee);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(employeeUuid, result.getId()),
                () -> assertEquals(userUuid, result.getUserId()),
                () -> assertEquals("John", result.getFirstName()),
                () -> assertEquals("Doe", result.getLastName()),
                () -> assertEquals("123456789", result.getPersonalId()),
                () -> assertEquals(EmployeeStatusEnum.ACTIVE, result.getStatus()),
                () -> assertEquals(1000L, result.getEmployeeNumber()),
                () -> assertEquals(EmployeeGenderEnum.MALE, result.getGender()),
                () -> assertEquals("Macedonian", result.getNationality()),
                () -> assertNull(result.getEmployeeContacts()),
                () -> assertNull(result.getEmployeeAddress()),
                () -> assertNull(result.getEmployeeFinance()),
                () -> assertNull(result.getEmployeeInfo()),
                () -> assertNull(result.getEmployeeReport()));
    }

    @Test
    void toDtoDetails_shouldReturnNullWhenEntityIsNull() {
        assertNull(employeeMapper.toDtoDetails(null));
    }

    @Test
    void toEntityCreate_shouldMapAllowedFieldsAndIgnoreGeneratedFields() {
        CreateEmployeeDto dto =
                mockCreateEmployeeDto("Jane", "Smith", "111222333", EmployeeGenderEnum.FEMALE, "Macedonian", userUuid);

        Employee result = employeeMapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeNumber()),
                () -> assertEquals(EmployeeStatusEnum.ACTIVE, result.getStatus()),
                () -> assertEquals(userUuid, result.getUserUuid()),
                () -> assertEquals("Jane", result.getFirstName()),
                () -> assertEquals("Smith", result.getLastName()),
                () -> assertEquals("111222333", result.getPersonalId()),
                () -> assertEquals(EmployeeGenderEnum.FEMALE, result.getGender()),
                () -> assertEquals("Macedonian", result.getNationality()));
    }

    @Test
    void toEntityCreate_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeMapper.toEntityCreate(null));
    }

    @Test
    void toEntityCreate_shouldHandleNullUserId() {
        CreateEmployeeDto dto = mockCreateEmployeeDto("John", "Doe", null, EmployeeGenderEnum.MALE, "Macedonian", null);

        Employee result = employeeMapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUserUuid()),
                () -> assertEquals("John", result.getFirstName()),
                () -> assertEquals("Doe", result.getLastName()));
    }

    @Test
    void toEntityUpdate_shouldMapAllowedFieldsAndIgnoreGeneratedFields() {
        UpdateEmployeeDto dto = mockUpdateEmployeeDto(
                "Updated",
                "Employee",
                "999888777",
                EmployeeGenderEnum.OTHER,
                "English",
                EmployeeStatusEnum.ARCHIVED,
                userUuid);

        Employee result = employeeMapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeNumber()),
                () -> assertEquals(userUuid, result.getUserUuid()),
                () -> assertEquals("Updated", result.getFirstName()),
                () -> assertEquals("Employee", result.getLastName()),
                () -> assertEquals("999888777", result.getPersonalId()),
                () -> assertEquals(EmployeeGenderEnum.OTHER, result.getGender()),
                () -> assertEquals("English", result.getNationality()),
                () -> assertEquals(EmployeeStatusEnum.ARCHIVED, result.getStatus()));
    }

    @Test
    void toEntityUpdate_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeMapper.toEntityUpdate(null));
    }

    @Test
    void toEntityUpdate_shouldHandleNullUserId() {
        UpdateEmployeeDto dto = mockUpdateEmployeeDto(
                "John", "Doe", null, EmployeeGenderEnum.MALE, "Macedonian", EmployeeStatusEnum.ACTIVE, null);

        Employee result = employeeMapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUserUuid()),
                () -> assertEquals(EmployeeStatusEnum.ACTIVE, result.getStatus()));
    }

    @Test
    void toDeletedDto_shouldSetUuid() {
        DeleteDto result = employeeMapper.toDeletedDto(employeeUuid);

        assertAll(() -> assertNotNull(result), () -> assertEquals(employeeUuid, result.getUuid()));
    }

    @Test
    void toDeletedDto_shouldReturnNullWhenUuidIsNull() {
        assertNull(employeeMapper.toDeletedDto(null));
    }

    @Test
    void toListDto_shouldMapAllowedFieldsAndIgnoreEmailAndMunicipality() {
        EmployeeListDto result = employeeMapper.toListDto(employee);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(employeeUuid, result.getId()),
                () -> assertEquals(1000L, result.getEmployeeNumber()),
                () -> assertEquals("John", result.getFirstName()),
                () -> assertEquals("Doe", result.getLastName()),
                () -> assertEquals(EmployeeStatusEnum.ACTIVE, result.getStatus()),
                () -> assertNull(result.getEmail()),
                () -> assertNull(result.getMunicipality()));
    }

    @Test
    void toListDto_shouldReturnNullWhenEntityIsNull() {
        assertNull(employeeMapper.toListDto(null));
    }

    @Test
    void toDtoList_shouldMapAllEmployees() {
        Employee secondEmployee = mockEmployee(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "987654321",
                EmployeeStatusEnum.ARCHIVED,
                2000L,
                EmployeeGenderEnum.FEMALE,
                "English");

        List<EmployeeListDto> result = employeeMapper.toDtoList(List.of(employee, secondEmployee));

        assertEquals(2, result.size());

        assertEmployeeListDto(result.get(0), employee);
        assertEmployeeListDto(result.get(1), secondEmployee);
    }

    @Test
    void toDtoList_shouldReturnEmptyListWhenInputIsEmpty() {
        List<EmployeeListDto> result = employeeMapper.toDtoList(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnNullWhenInputIsNull() {
        assertNull(employeeMapper.toDtoList(null));
    }

    private void assertEmployeeListDto(EmployeeListDto result, Employee employee) {

        assertAll(
                () -> assertEquals(employee.getUuid(), result.getId()),
                () -> assertEquals(employee.getEmployeeNumber(), result.getEmployeeNumber()),
                () -> assertEquals(employee.getFirstName(), result.getFirstName()),
                () -> assertEquals(employee.getLastName(), result.getLastName()),
                () -> assertEquals(employee.getStatus(), result.getStatus()));
    }

    private Employee mockEmployee(
            UUID uuid,
            UUID userUuid,
            String firstName,
            String lastName,
            String personalId,
            EmployeeStatusEnum status,
            Long employeeNumber,
            EmployeeGenderEnum gender,
            String nationality) {

        return Employee.builder()
                .uuid(uuid)
                .userUuid(userUuid)
                .firstName(firstName)
                .lastName(lastName)
                .personalId(personalId)
                .status(status)
                .employeeNumber(employeeNumber)
                .gender(gender)
                .nationality(nationality)
                .build();
    }

    private EmployeeDto mockEmployeeDto(
            UUID uuid,
            UUID userUuid,
            String firstName,
            String lastName,
            String personalId,
            EmployeeStatusEnum status,
            Long employeeNumber,
            EmployeeGenderEnum gender,
            String nationality) {

        return EmployeeDto.builder()
                .id(uuid)
                .userId(userUuid)
                .firstName(firstName)
                .lastName(lastName)
                .personalId(personalId)
                .status(status)
                .employeeNumber(employeeNumber)
                .gender(gender)
                .nationality(nationality)
                .build();
    }

    private CreateEmployeeDto mockCreateEmployeeDto(
            String firstName,
            String lastName,
            String personalId,
            EmployeeGenderEnum gender,
            String nationality,
            UUID userId) {

        return CreateEmployeeDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .personalId(personalId)
                .gender(gender)
                .nationality(nationality)
                .userId(userId)
                .build();
    }

    private UpdateEmployeeDto mockUpdateEmployeeDto(
            String firstName,
            String lastName,
            String personalId,
            EmployeeGenderEnum gender,
            String nationality,
            EmployeeStatusEnum status,
            UUID userId) {

        return UpdateEmployeeDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .personalId(personalId)
                .gender(gender)
                .nationality(nationality)
                .status(status)
                .userId(userId)
                .build();
    }
}
