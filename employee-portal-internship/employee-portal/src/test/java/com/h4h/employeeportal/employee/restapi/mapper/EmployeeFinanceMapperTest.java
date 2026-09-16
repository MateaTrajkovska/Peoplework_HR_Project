package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.enumeration.BankStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeFinance;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeFinanceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeFinanceMapperTest {

    private EmployeeFinanceMapper employeeFinanceMapper;

    private UUID financeUuid;
    private UUID employeeUuid;

    private EmployeeFinance employeeFinance;

    @BeforeEach
    void setUp() {
        employeeFinanceMapper = new EmployeeFinanceMapperImpl();

        financeUuid = UUID.randomUUID();
        employeeUuid = UUID.randomUUID();

        employeeFinance = mockEmployeeFinance(financeUuid, employeeUuid, 123456789L, BankStatusEnum.ACTIVE);
    }

    @Test
    void toDto_shouldMapAllFields() {
        EmployeeFinanceDto result = employeeFinanceMapper.toDto(employeeFinance);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(financeUuid, result.getId()),
                () -> assertEquals(employeeUuid, result.getEmployeeId()),
                () -> assertEquals(123456789L, result.getBankAccount()),
                () -> assertEquals(BankStatusEnum.ACTIVE, result.getBankStatus()));
    }

    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        assertNull(employeeFinanceMapper.toDto(null));
    }

    @Test
    void toDto_shouldMapNullValues() {
        EmployeeFinance finance = mockEmployeeFinance(financeUuid, employeeUuid, null, null);

        EmployeeFinanceDto result = employeeFinanceMapper.toDto(finance);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(financeUuid, result.getId()),
                () -> assertEquals(employeeUuid, result.getEmployeeId()),
                () -> assertNull(result.getBankAccount()),
                () -> assertNull(result.getBankStatus()));
    }

    @Test
    void toEntity_shouldMapAllFields() {
        EmployeeFinanceDto dto = mockEmployeeFinanceDto(financeUuid, employeeUuid, 987654321L, BankStatusEnum.ACTIVE);

        EmployeeFinance result = employeeFinanceMapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(financeUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals(987654321L, result.getBankAccount()),
                () -> assertEquals(BankStatusEnum.ACTIVE, result.getBankStatus()));
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeFinanceMapper.toEntity(null));
    }

    @Test
    void toEntity_shouldMapNullValues() {
        EmployeeFinanceDto dto = mockEmployeeFinanceDto(null, null, null, null);

        EmployeeFinance result = employeeFinanceMapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getBankAccount()),
                () -> assertNull(result.getBankStatus()));
    }

    @Test
    void toEntityCreate_shouldMapFieldsAndIgnoreGeneratedStatus() {
        CreateEmployeeFinanceDto dto = mockCreateEmployeeFinanceDto(employeeUuid, 123456789L);

        EmployeeFinance result = employeeFinanceMapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals(123456789L, result.getBankAccount()),
                () -> assertNull(result.getUuid()),
                // Status is set by the database/Liquibase, not the mapper.
                () -> assertNull(result.getBankStatus()));
    }

    @Test
    void toEntityCreate_shouldMapNullValues() {
        CreateEmployeeFinanceDto dto = mockCreateEmployeeFinanceDto(null, null);

        EmployeeFinance result = employeeFinanceMapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getBankAccount()),
                () -> assertNull(result.getBankStatus()));
    }

    @Test
    void toEntityCreate_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeFinanceMapper.toEntityCreate(null));
    }

    @Test
    void toEntityCreateFromEmployee_shouldMapBankAccountAndIgnoreOtherFields() {
        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setNationality("Macedonian");
        dto.setBankAccount(123456789L);

        EmployeeFinance result = employeeFinanceMapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(123456789L, result.getBankAccount()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getBankStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldHandleNullBankAccount() {
        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPersonalId("123456789");
        dto.setNationality("Macedonian");
        dto.setBankAccount(null);

        EmployeeFinance result = employeeFinanceMapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getBankAccount()),
                () -> assertNull(result.getBankStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeFinanceMapper.toEntityCreateFromEmployee(null));
    }

    @Test
    void toEntityUpdate_shouldMapFieldsAndIgnoreIds() {
        UpdateEmployeeFinanceDto dto = mockUpdateEmployeeFinanceDto(987654321L, BankStatusEnum.INACTIVE);

        EmployeeFinance result = employeeFinanceMapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(987654321L, result.getBankAccount()),
                () -> assertEquals(BankStatusEnum.INACTIVE, result.getBankStatus()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()));
    }

    @Test
    void toEntityUpdate_shouldMapNullValues() {
        UpdateEmployeeFinanceDto dto = mockUpdateEmployeeFinanceDto(null, null);

        EmployeeFinance result = employeeFinanceMapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getBankAccount()),
                () -> assertNull(result.getBankStatus()));
    }

    @Test
    void toEntityUpdate_shouldReturnNullWhenDtoIsNull() {
        assertNull(employeeFinanceMapper.toEntityUpdate(null));
    }

    @Test
    void toDeletedDto_shouldSetUuid() {
        DeleteDto result = employeeFinanceMapper.toDeletedDto(financeUuid);

        assertAll(() -> assertNotNull(result), () -> assertEquals(financeUuid, result.getUuid()));
    }

    @Test
    void toDeletedDto_shouldReturnNullWhenUuidIsNull() {
        assertNull(employeeFinanceMapper.toDeletedDto(null));
    }

    @Test
    void toDtoList_shouldMapAllEntities() {
        EmployeeFinance secondFinance =
                mockEmployeeFinance(UUID.randomUUID(), UUID.randomUUID(), 987654321L, BankStatusEnum.INACTIVE);

        List<EmployeeFinanceDto> result = employeeFinanceMapper.toDtoList(List.of(employeeFinance, secondFinance));

        assertEquals(2, result.size());

        assertFinanceDto(result.get(0), employeeFinance);
        assertFinanceDto(result.get(1), secondFinance);
    }

    @Test
    void toDtoList_shouldReturnEmptyListWhenInputIsEmpty() {
        List<EmployeeFinanceDto> result = employeeFinanceMapper.toDtoList(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnNullWhenInputListIsNull() {
        assertNull(employeeFinanceMapper.toDtoList(null));
    }

    private void assertFinanceDto(EmployeeFinanceDto result, EmployeeFinance finance) {

        assertAll(
                () -> assertEquals(finance.getUuid(), result.getId()),
                () -> assertEquals(finance.getEmployeeUuid(), result.getEmployeeId()),
                () -> assertEquals(finance.getBankAccount(), result.getBankAccount()),
                () -> assertEquals(finance.getBankStatus(), result.getBankStatus()));
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

    private EmployeeFinanceDto mockEmployeeFinanceDto(
            UUID uuid, UUID employeeUuid, Long bankAccount, BankStatusEnum bankStatus) {

        return EmployeeFinanceDto.builder()
                .id(uuid)
                .employeeId(employeeUuid)
                .bankAccount(bankAccount)
                .bankStatus(bankStatus)
                .build();
    }

    private CreateEmployeeFinanceDto mockCreateEmployeeFinanceDto(UUID employeeId, Long bankAccount) {

        return CreateEmployeeFinanceDto.builder()
                .employeeId(employeeId)
                .bankAccount(bankAccount)
                .build();
    }

    private UpdateEmployeeFinanceDto mockUpdateEmployeeFinanceDto(Long bankAccount, BankStatusEnum bankStatus) {

        return UpdateEmployeeFinanceDto.builder()
                .bankAccount(bankAccount)
                .bankStatus(bankStatus)
                .build();
    }
}
