package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeAddressStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeAddress;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeAddressDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeAddressMapperTest {

    private EmployeeAddressMapper employeeAddressMapper;

    private UUID addressUuid;
    private UUID employeeUuid;

    private EmployeeAddress employeeAddress;

    @BeforeEach
    void setUp() {
        employeeAddressMapper = new EmployeeAddressMapperImpl();

        addressUuid = UUID.randomUUID();
        employeeUuid = UUID.randomUUID();

        employeeAddress = mockEmployeeAddress(addressUuid, employeeUuid, "1000", "Skopje", "Main Street 1");
    }

    @Test
    void toDto_shouldMapAllFields() {
        EmployeeAddressDto result = employeeAddressMapper.toDto(employeeAddress);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(addressUuid.toString(), result.getId()),
                () -> assertEquals(employeeUuid.toString(), result.getEmployeeId()),
                () -> assertEquals("1000", result.getPostalCode()),
                () -> assertEquals("Skopje", result.getMunicipality()),
                () -> assertEquals("Main Street 1", result.getStreet()));
    }

    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        assertNull(employeeAddressMapper.toDto(null));
    }

    @Test
    void toDto_shouldMapNullOptionalValues() {
        EmployeeAddress address = mockEmployeeAddress(addressUuid, employeeUuid, null, null, null);

        EmployeeAddressDto result = employeeAddressMapper.toDto(address);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(addressUuid.toString(), result.getId()),
                () -> assertEquals(employeeUuid.toString(), result.getEmployeeId()),
                () -> assertNull(result.getPostalCode()),
                () -> assertNull(result.getMunicipality()),
                () -> assertNull(result.getStreet()));
    }

    @Test
    void toEntity_shouldMapAllFieldsAndIgnoreStatus() {
        EmployeeAddressDto dto = mockEmployeeAddressDto(addressUuid, employeeUuid, "1000", "Skopje", "Main Street 1");

        EmployeeAddress result = employeeAddressMapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(addressUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals("1000", result.getPostalCode()),
                () -> assertEquals("Skopje", result.getMunicipality()),
                () -> assertEquals("Main Street 1", result.getStreet()),
                () -> assertNull(result.getAddressStatus()));
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeAddressMapper.toEntity(null));
    }

    @Test
    void toEntity_shouldHandleNullOptionalFields() {
        EmployeeAddressDto dto = mockEmployeeAddressDto(addressUuid, employeeUuid, null, null, null);

        EmployeeAddress result = employeeAddressMapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(addressUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertNull(result.getPostalCode()),
                () -> assertNull(result.getMunicipality()),
                () -> assertNull(result.getStreet()),
                () -> assertNull(result.getAddressStatus()));
    }

    @Test
    void toEntityCreate_shouldMapFieldsAndIgnoreGeneratedFields() {
        CreateEmployeeAddressDto dto = mockCreateEmployeeAddressDto(employeeUuid, "1000", "Skopje", "Main Street 1");

        EmployeeAddress result = employeeAddressMapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals("1000", result.getPostalCode()),
                () -> assertEquals("Skopje", result.getMunicipality()),
                () -> assertEquals("Main Street 1", result.getStreet()),
                () -> assertNull(result.getAddressStatus()));
    }

    @Test
    void toEntityCreate_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeAddressMapper.toEntityCreate(null));
    }

    @Test
    void toEntityCreate_shouldHandleNullEmployeeId() {
        CreateEmployeeAddressDto dto = mockCreateEmployeeAddressDto(null, "1000", "Skopje", "Main Street 1");

        EmployeeAddress result = employeeAddressMapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertEquals("1000", result.getPostalCode()),
                () -> assertEquals("Skopje", result.getMunicipality()),
                () -> assertEquals("Main Street 1", result.getStreet()),
                () -> assertNull(result.getAddressStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldMapAddressFieldsAndIgnoreOtherFields() {
        CreateEmployeeDto dto = new CreateEmployeeDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPersonalId("123456789");
        dto.setNationality("Macedonian");
        dto.setPostalCode("2000");
        dto.setMunicipality("Bitola");
        dto.setStreet("Second Street 10");

        EmployeeAddress result = employeeAddressMapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("2000", result.getPostalCode()),
                () -> assertEquals("Bitola", result.getMunicipality()),
                () -> assertEquals("Second Street 10", result.getStreet()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getAddressStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldHandleNullAddressFields() {
        EmployeeAddress result = employeeAddressMapper.toEntityCreateFromEmployee(new CreateEmployeeDto());

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getPostalCode()),
                () -> assertNull(result.getMunicipality()),
                () -> assertNull(result.getStreet()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getAddressStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeAddressMapper.toEntityCreateFromEmployee(null));
    }

    @Test
    void toEntityUpdate_shouldMapUpdatableFieldsAndIgnoreOtherFields() {
        UpdateEmployeeAddressDto dto = mockUpdateEmployeeAddressDto("3000", "Ohrid", "Lake Street 20");

        EmployeeAddress result = employeeAddressMapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("3000", result.getPostalCode()),
                () -> assertEquals("Ohrid", result.getMunicipality()),
                () -> assertEquals("Lake Street 20", result.getStreet()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getAddressStatus()));
    }

    @Test
    void toEntityUpdate_shouldHandleNullOptionalFields() {
        EmployeeAddress result = employeeAddressMapper.toEntityUpdate(mockUpdateEmployeeAddressDto(null, null, null));

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getPostalCode()),
                () -> assertNull(result.getMunicipality()),
                () -> assertNull(result.getStreet()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getAddressStatus()));
    }

    @Test
    void toEntityUpdate_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeAddressMapper.toEntityUpdate(null));
    }

    @Test
    void toDeletedDto_shouldSetUuid() {
        DeleteDto result = employeeAddressMapper.toDeletedDto(addressUuid);

        assertAll(() -> assertNotNull(result), () -> assertEquals(addressUuid, result.getUuid()));
    }

    @Test
    void toDeletedDto_shouldReturnNullWhenUuidIsNull() {
        assertNull(employeeAddressMapper.toDeletedDto(null));
    }

    @Test
    void toDtoList_shouldMapAllEntities() {
        EmployeeAddress secondAddress =
                mockEmployeeAddress(UUID.randomUUID(), UUID.randomUUID(), "2000", "Bitola", "Second Street 2");

        List<EmployeeAddressDto> result = employeeAddressMapper.toDtoList(List.of(employeeAddress, secondAddress));

        assertEquals(2, result.size());

        assertAddressDto(result.get(0), employeeAddress);
        assertAddressDto(result.get(1), secondAddress);
    }

    @Test
    void toDtoList_shouldReturnEmptyListWhenInputListIsEmpty() {
        List<EmployeeAddressDto> result = employeeAddressMapper.toDtoList(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnNullWhenInputListIsNull() {
        assertNull(employeeAddressMapper.toDtoList(null));
    }

    private void assertAddressDto(EmployeeAddressDto result, EmployeeAddress address) {

        assertAll(
                () -> assertEquals(address.getUuid().toString(), result.getId()),
                () -> assertEquals(address.getEmployeeUuid().toString(), result.getEmployeeId()),
                () -> assertEquals(address.getPostalCode(), result.getPostalCode()),
                () -> assertEquals(address.getMunicipality(), result.getMunicipality()),
                () -> assertEquals(address.getStreet(), result.getStreet()));
    }

    private EmployeeAddress mockEmployeeAddress(
            UUID uuid, UUID employeeUuid, String postalCode, String municipality, String street) {

        return EmployeeAddress.builder()
                .uuid(uuid)
                .employeeUuid(employeeUuid)
                .postalCode(postalCode)
                .municipality(municipality)
                .street(street)
                .addressStatus(EmployeeAddressStatusEnum.ACTIVE)
                .build();
    }

    private EmployeeAddressDto mockEmployeeAddressDto(
            UUID uuid, UUID employeeUuid, String postalCode, String municipality, String street) {

        return EmployeeAddressDto.builder()
                .id(uuid.toString())
                .employeeId(employeeUuid.toString())
                .postalCode(postalCode)
                .municipality(municipality)
                .street(street)
                .build();
    }

    private CreateEmployeeAddressDto mockCreateEmployeeAddressDto(
            UUID employeeId, String postalCode, String municipality, String street) {

        return CreateEmployeeAddressDto.builder()
                .employeeId(employeeId)
                .postalCode(postalCode)
                .municipality(municipality)
                .street(street)
                .build();
    }

    private UpdateEmployeeAddressDto mockUpdateEmployeeAddressDto(
            String postalCode, String municipality, String street) {

        return UpdateEmployeeAddressDto.builder()
                .postalCode(postalCode)
                .municipality(municipality)
                .street(street)
                .build();
    }
}
