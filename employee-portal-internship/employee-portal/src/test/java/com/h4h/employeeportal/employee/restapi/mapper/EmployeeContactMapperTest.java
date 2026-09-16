package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactStatusEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactTypeEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeContact;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeContactDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeContactMapperTest {

    private EmployeeContactMapper employeeContactMapper;

    private UUID contactUuid;
    private UUID employeeUuid;

    private EmployeeContact employeeContact;

    @BeforeEach
    void setUp() {
        employeeContactMapper = new EmployeeContactMapperImpl();

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

    @Test
    void toDto_shouldMapAllFields() {
        EmployeeContactDto result = employeeContactMapper.toDto(employeeContact);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(contactUuid, result.getId()),
                () -> assertEquals(employeeUuid, result.getEmployeeId()),
                () -> assertEquals("john.doe@example.com", result.getEmail()),
                () -> assertEquals("+38970123456", result.getPhoneNumber()),
                () -> assertEquals(EmployeeContactTypeEnum.PERSONAL, result.getContactType()),
                () -> assertEquals(EmployeeContactStatusEnum.ACTIVE, result.getContactStatus()));
    }

    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        assertNull(employeeContactMapper.toDto(null));
    }

    @Test
    void toDto_shouldMapNullValues() {
        EmployeeContact contact = mockEmployeeContact(contactUuid, employeeUuid, null, null, null, null);

        EmployeeContactDto result = employeeContactMapper.toDto(contact);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(contactUuid, result.getId()),
                () -> assertEquals(employeeUuid, result.getEmployeeId()),
                () -> assertNull(result.getEmail()),
                () -> assertNull(result.getPhoneNumber()),
                () -> assertNull(result.getContactType()),
                () -> assertNull(result.getContactStatus()));
    }

    @Test
    void toEntity_shouldMapAllFields() {
        EmployeeContactDto dto = mockEmployeeContactDto(
                contactUuid,
                employeeUuid,
                "john.doe@example.com",
                "+38970123456",
                EmployeeContactTypeEnum.PERSONAL,
                EmployeeContactStatusEnum.ACTIVE);

        EmployeeContact result = employeeContactMapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(contactUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals("john.doe@example.com", result.getEmail()),
                () -> assertEquals("+38970123456", result.getPhoneNumber()),
                () -> assertEquals(EmployeeContactTypeEnum.PERSONAL, result.getContactType()),
                () -> assertEquals(EmployeeContactStatusEnum.ACTIVE, result.getContactStatus()));
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeContactMapper.toEntity(null));
    }

    @Test
    void toEntity_shouldMapNullValues() {
        EmployeeContact result =
                employeeContactMapper.toEntity(mockEmployeeContactDto(null, null, null, null, null, null));

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getEmail()),
                () -> assertNull(result.getPhoneNumber()),
                () -> assertNull(result.getContactType()),
                () -> assertNull(result.getContactStatus()));
    }

    @Test
    void toEntityCreate_shouldMapFieldsAndIgnoreGeneratedFields() {
        CreateEmployeeContactDto dto =
                mockCreateEmployeeContactDto("john.doe@example.com", "+38970123456", EmployeeContactTypeEnum.PERSONAL);

        EmployeeContact result = employeeContactMapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("john.doe@example.com", result.getEmail()),
                () -> assertEquals("+38970123456", result.getPhoneNumber()),
                () -> assertEquals(EmployeeContactTypeEnum.PERSONAL, result.getContactType()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getContactStatus()));
    }

    @Test
    void toEntityCreate_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeContactMapper.toEntityCreate(null));
    }

    @Test
    void toEntityCreate_shouldHandleNullContactType() {
        CreateEmployeeContactDto dto = mockCreateEmployeeContactDto("john.doe@example.com", "+38970123456", null);

        EmployeeContact result = employeeContactMapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("john.doe@example.com", result.getEmail()),
                () -> assertEquals("+38970123456", result.getPhoneNumber()),
                () -> assertNull(result.getContactType()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getContactStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldIgnoreEmployeeAndContactFields() {
        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setNationality("Macedonian");

        CreateEmployeeContactDto contact =
                mockCreateEmployeeContactDto("john.doe@company.com", "+38970123456", EmployeeContactTypeEnum.CORPORATE);

        dto.setEmployeeContacts(List.of(contact));

        EmployeeContact result = employeeContactMapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getEmail()),
                () -> assertNull(result.getPhoneNumber()),
                () -> assertNull(result.getContactType()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getContactStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeContactMapper.toEntityCreateFromEmployee(null));
    }

    @Test
    void toEntityCreateFromEmployee_shouldHandleEmptyContacts() {
        CreateEmployeeDto dto = new CreateEmployeeDto();
        dto.setEmployeeContacts(List.of());

        EmployeeContact result = employeeContactMapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getEmail()),
                () -> assertNull(result.getPhoneNumber()),
                () -> assertNull(result.getContactType()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getContactStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldHandleNullContacts() {
        CreateEmployeeDto dto = new CreateEmployeeDto();
        dto.setEmployeeContacts(null);

        EmployeeContact result = employeeContactMapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getEmail()),
                () -> assertNull(result.getPhoneNumber()),
                () -> assertNull(result.getContactType()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getContactStatus()));
    }

    @Test
    void toEntityUpdate_shouldMapFieldsAndIgnoreEmployeeUuid() {
        UpdateEmployeeContactDto dto = mockUpdateEmployeeContactDto(
                contactUuid,
                "updated@example.com",
                "+38970987654",
                EmployeeContactTypeEnum.CORPORATE,
                EmployeeContactStatusEnum.INACTIVE);

        EmployeeContact result = employeeContactMapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(contactUuid, result.getUuid()),
                () -> assertEquals("updated@example.com", result.getEmail()),
                () -> assertEquals("+38970987654", result.getPhoneNumber()),
                () -> assertEquals(EmployeeContactTypeEnum.CORPORATE, result.getContactType()),
                () -> assertEquals(EmployeeContactStatusEnum.INACTIVE, result.getContactStatus()),
                () -> assertNull(result.getEmployeeUuid()));
    }

    @Test
    void toEntityUpdate_shouldMapNullValues() {
        EmployeeContact result =
                employeeContactMapper.toEntityUpdate(mockUpdateEmployeeContactDto(null, null, null, null, null));

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getEmail()),
                () -> assertNull(result.getPhoneNumber()),
                () -> assertNull(result.getContactType()),
                () -> assertNull(result.getContactStatus()));
    }

    @Test
    void toEntityUpdate_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeContactMapper.toEntityUpdate(null));
    }

    @Test
    void toDeletedDto_shouldSetUuid() {
        DeleteDto result = employeeContactMapper.toDeletedDto(contactUuid);

        assertAll(() -> assertNotNull(result), () -> assertEquals(contactUuid, result.getUuid()));
    }

    @Test
    void toDeletedDto_shouldReturnNullWhenUuidIsNull() {
        assertNull(employeeContactMapper.toDeletedDto(null));
    }

    @Test
    void toDtoList_shouldMapAllEntities() {
        EmployeeContact secondContact = mockEmployeeContact(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "jane.doe@example.com",
                "+38970999999",
                EmployeeContactTypeEnum.CORPORATE,
                EmployeeContactStatusEnum.ACTIVE);

        List<EmployeeContactDto> result = employeeContactMapper.toDtoList(List.of(employeeContact, secondContact));

        assertEquals(2, result.size());

        assertContactDto(result.get(0), employeeContact);
        assertContactDto(result.get(1), secondContact);
    }

    @Test
    void toDtoList_shouldReturnEmptyListWhenInputListIsEmpty() {
        List<EmployeeContactDto> result = employeeContactMapper.toDtoList(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnNullWhenInputListIsNull() {
        assertNull(employeeContactMapper.toDtoList(null));
    }

    private void assertContactDto(EmployeeContactDto result, EmployeeContact contact) {

        assertAll(
                () -> assertEquals(contact.getUuid(), result.getId()),
                () -> assertEquals(contact.getEmployeeUuid(), result.getEmployeeId()),
                () -> assertEquals(contact.getEmail(), result.getEmail()),
                () -> assertEquals(contact.getPhoneNumber(), result.getPhoneNumber()),
                () -> assertEquals(contact.getContactType(), result.getContactType()),
                () -> assertEquals(contact.getContactStatus(), result.getContactStatus()));
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

    private EmployeeContactDto mockEmployeeContactDto(
            UUID uuid,
            UUID employeeUuid,
            String email,
            String phoneNumber,
            EmployeeContactTypeEnum contactType,
            EmployeeContactStatusEnum contactStatus) {

        return EmployeeContactDto.builder()
                .id(uuid)
                .employeeId(employeeUuid)
                .email(email)
                .phoneNumber(phoneNumber)
                .contactType(contactType)
                .contactStatus(contactStatus)
                .build();
    }

    private CreateEmployeeContactDto mockCreateEmployeeContactDto(
            String email, String phoneNumber, EmployeeContactTypeEnum contactType) {

        return CreateEmployeeContactDto.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .contactType(contactType)
                .build();
    }

    private UpdateEmployeeContactDto mockUpdateEmployeeContactDto(
            UUID uuid,
            String email,
            String phoneNumber,
            EmployeeContactTypeEnum contactType,
            EmployeeContactStatusEnum contactStatus) {

        return UpdateEmployeeContactDto.builder()
                .uuid(uuid)
                .email(email)
                .phoneNumber(phoneNumber)
                .contactType(contactType)
                .contactStatus(contactStatus)
                .build();
    }
}
