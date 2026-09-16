package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeReportStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeReport;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeReportDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeReportMapperTest {

    private EmployeeReportMapper mapper;

    private UUID reportUuid;
    private UUID employeeUuid;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EmployeeReportMapper.class);

        reportUuid = UUID.randomUUID();
        employeeUuid = UUID.randomUUID();
    }

    private EmployeeReport createEmployeeReport(
            UUID reportUuid, UUID employeeUuid, String internalNote, EmployeeReportStatusEnum status) {
        return new EmployeeReport(reportUuid, employeeUuid, internalNote, status);
    }

    @Test
    void toDto_shouldMapAllFields() {
        EmployeeReport entity = createEmployeeReport(
                reportUuid, employeeUuid, "Employee performance review", EmployeeReportStatusEnum.ACTIVE);

        EmployeeReportDto result = mapper.toDto(entity);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(reportUuid.toString(), result.getId()),
                () -> assertEquals(employeeUuid.toString(), result.getEmployeeId()),
                () -> assertEquals("Employee performance review", result.getInternalNote()),
                () -> assertEquals(EmployeeReportStatusEnum.ACTIVE, result.getReportStatus()));
    }

    @Test
    void toDto_shouldAllowNullInternalNote() {
        EmployeeReport entity = createEmployeeReport(reportUuid, employeeUuid, null, EmployeeReportStatusEnum.ACTIVE);

        EmployeeReportDto result = mapper.toDto(entity);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(reportUuid.toString(), result.getId()),
                () -> assertEquals(employeeUuid.toString(), result.getEmployeeId()),
                () -> assertNull(result.getInternalNote()),
                () -> assertEquals(EmployeeReportStatusEnum.ACTIVE, result.getReportStatus()));
    }

    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        EmployeeReportDto result = mapper.toDto(null);

        assertNull(result);
    }

    @Test
    void toEntity_shouldMapAllFields() {
        EmployeeReportDto dto = new EmployeeReportDto();

        dto.setId(reportUuid.toString());
        dto.setEmployeeId(employeeUuid.toString());
        dto.setInternalNote("Internal employee note");
        dto.setReportStatus(EmployeeReportStatusEnum.INACTIVE);

        EmployeeReport result = mapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(reportUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals("Internal employee note", result.getInternalNote()),
                () -> assertEquals(EmployeeReportStatusEnum.INACTIVE, result.getReportStatus()));
    }

    @Test
    void toEntity_shouldAllowNullInternalNote() {
        EmployeeReportDto dto = new EmployeeReportDto();

        dto.setId(reportUuid.toString());
        dto.setEmployeeId(employeeUuid.toString());
        dto.setInternalNote(null);
        dto.setReportStatus(EmployeeReportStatusEnum.ACTIVE);

        EmployeeReport result = mapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(reportUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertNull(result.getInternalNote()),
                () -> assertEquals(EmployeeReportStatusEnum.ACTIVE, result.getReportStatus()));
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        EmployeeReport result = mapper.toEntity(null);

        assertNull(result);
    }

    @Test
    void toEntityCreate_shouldMapAllAllowedFields() {
        CreateEmployeeReportDto dto = new CreateEmployeeReportDto();

        dto.setEmployeeId(employeeUuid);
        dto.setInternalNote("New employee report");

        EmployeeReport result = mapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals("New employee report", result.getInternalNote()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getReportStatus()));
    }

    @Test
    void toEntityCreate_shouldAllowNullInternalNote() {
        CreateEmployeeReportDto dto = new CreateEmployeeReportDto();

        dto.setEmployeeId(employeeUuid);
        dto.setInternalNote(null);

        EmployeeReport result = mapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertNull(result.getInternalNote()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getReportStatus()));
    }

    @Test
    void toEntityCreate_shouldIgnoreReportStatus() {
        CreateEmployeeReportDto dto = new CreateEmployeeReportDto();

        dto.setEmployeeId(employeeUuid);
        dto.setInternalNote("Test note");

        EmployeeReport result = mapper.toEntityCreate(dto);

        assertNull(result.getReportStatus());
    }

    @Test
    void toEntityCreate_shouldIgnoreUuid() {
        CreateEmployeeReportDto dto = new CreateEmployeeReportDto();

        dto.setEmployeeId(employeeUuid);
        dto.setInternalNote("Test note");

        EmployeeReport result = mapper.toEntityCreate(dto);

        assertNull(result.getUuid());
    }

    @Test
    void toEntityCreate_shouldReturnNullWhenDtoIsNull() {
        EmployeeReport result = mapper.toEntityCreate(null);

        assertNull(result);
    }

    @Test
    void toEntityCreateFromEmployee_shouldMapInternalNote() {
        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setInternalNote("Employee internal note");

        EmployeeReport result = mapper.toEntityCreateFromEmployee(dto);

        assertAll(() -> assertNotNull(result), () -> assertEquals("Employee internal note", result.getInternalNote()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldIgnoreEmployeeFields() {
        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setInternalNote("Employee report");

        EmployeeReport result = mapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getReportStatus()),
                () -> assertEquals("Employee report", result.getInternalNote()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldAllowNullInternalNote() {
        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setInternalNote(null);

        EmployeeReport result = mapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getInternalNote()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getReportStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldReturnNullWhenDtoIsNull() {
        EmployeeReport result = mapper.toEntityCreateFromEmployee(null);

        assertNull(result);
    }

    @Test
    void toEntityUpdate_shouldMapAllAllowedFields() {
        UpdateEmployeeReportDto dto = new UpdateEmployeeReportDto();

        dto.setInternalNote("Updated report");
        dto.setReportStatus(EmployeeReportStatusEnum.INACTIVE);

        EmployeeReport result = mapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Updated report", result.getInternalNote()),
                () -> assertEquals(EmployeeReportStatusEnum.INACTIVE, result.getReportStatus()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()));
    }

    @Test
    void toEntityUpdate_shouldAllowNullInternalNote() {
        UpdateEmployeeReportDto dto = new UpdateEmployeeReportDto();

        dto.setInternalNote(null);
        dto.setReportStatus(EmployeeReportStatusEnum.ACTIVE);

        EmployeeReport result = mapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getInternalNote()),
                () -> assertEquals(EmployeeReportStatusEnum.ACTIVE, result.getReportStatus()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()));
    }

    @Test
    void toEntityUpdate_shouldIgnoreUuidAndEmployeeUuid() {
        UpdateEmployeeReportDto dto = new UpdateEmployeeReportDto();

        dto.setInternalNote("Updated note");
        dto.setReportStatus(EmployeeReportStatusEnum.ACTIVE);

        EmployeeReport result = mapper.toEntityUpdate(dto);

        assertAll(() -> assertNull(result.getUuid()), () -> assertNull(result.getEmployeeUuid()));
    }

    @Test
    void toEntityUpdate_shouldReturnNullWhenDtoIsNull() {
        EmployeeReport result = mapper.toEntityUpdate(null);

        assertNull(result);
    }

    @Test
    void toDeletedDto_shouldMapUuid() {
        DeleteDto result = mapper.toDeletedDto(reportUuid);

        assertAll(() -> assertNotNull(result), () -> assertEquals(reportUuid, result.getUuid()));
    }

    @Test
    void toDeletedDto_shouldReturnNullWhenUuidIsNull() {
        DeleteDto result = mapper.toDeletedDto(null);

        assertNull(result);
    }

    @Test
    void toDtoList_shouldMapAllEntities() {
        EmployeeReport firstEntity =
                createEmployeeReport(reportUuid, employeeUuid, "First report", EmployeeReportStatusEnum.ACTIVE);

        UUID secondReportUuid = UUID.randomUUID();

        EmployeeReport secondEntity = createEmployeeReport(
                secondReportUuid, employeeUuid, "Second report", EmployeeReportStatusEnum.INACTIVE);

        List<EmployeeReportDto> result = mapper.toDtoList(List.of(firstEntity, secondEntity));

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals(reportUuid.toString(), result.getFirst().getId()),
                () -> assertEquals(employeeUuid.toString(), result.getFirst().getEmployeeId()),
                () -> assertEquals("First report", result.getFirst().getInternalNote()),
                () -> assertEquals(
                        EmployeeReportStatusEnum.ACTIVE, result.getFirst().getReportStatus()),
                () -> assertEquals(secondReportUuid.toString(), result.get(1).getId()),
                () -> assertEquals(employeeUuid.toString(), result.get(1).getEmployeeId()),
                () -> assertEquals("Second report", result.get(1).getInternalNote()),
                () -> assertEquals(
                        EmployeeReportStatusEnum.INACTIVE, result.get(1).getReportStatus()));
    }

    @Test
    void toDtoList_shouldReturnEmptyListWhenEntitiesAreEmpty() {
        List<EmployeeReportDto> result = mapper.toDtoList(Collections.emptyList());

        assertAll(() -> assertNotNull(result), () -> assertTrue(result.isEmpty()));
    }

    @Test
    void toDtoList_shouldReturnNullWhenEntitiesAreNull() {
        List<EmployeeReportDto> result = mapper.toDtoList(null);

        assertNull(result);
    }

    @Test
    void toDtoList_shouldHandleEntityWithNullOptionalField() {
        EmployeeReport entity = createEmployeeReport(reportUuid, employeeUuid, null, EmployeeReportStatusEnum.ACTIVE);

        List<EmployeeReportDto> result = mapper.toDtoList(List.of(entity));

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertNull(result.getFirst().getInternalNote()),
                () -> assertEquals(reportUuid.toString(), result.getFirst().getId()));
    }
}
