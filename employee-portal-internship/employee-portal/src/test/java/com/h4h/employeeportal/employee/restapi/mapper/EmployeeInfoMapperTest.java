package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeGenderEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeInfoStatusEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeePositionEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeTypeEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeInfo;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeInfoDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeInfoMapperTest {

    private final EmployeeInfoMapper mapper = Mappers.getMapper(EmployeeInfoMapper.class);

    @Test
    void toDto_shouldMapAllFields() {
        UUID infoUuid = UUID.randomUUID();
        UUID employeeUuid = UUID.randomUUID();

        LocalDate dateOfBirth = LocalDate.of(1995, 5, 10);
        LocalDate startDate = LocalDate.of(2020, 1, 15);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        Period priorExperience = Period.ofYears(3);

        EmployeeInfo entity = mockEmployeeInfo(
                infoUuid,
                employeeUuid,
                dateOfBirth,
                EmployeeTypeEnum.FULL_TIME,
                startDate,
                endDate,
                "ID123456",
                priorExperience,
                EmployeePositionEnum.BE_DEV,
                EmployeeInfoStatusEnum.ACTIVE);

        EmployeeInfoDto result = mapper.toDto(entity);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(infoUuid.toString(), result.getId()),
                () -> assertEquals(employeeUuid.toString(), result.getEmployeeId()),
                () -> assertEquals(dateOfBirth, result.getDateOfBirth()),
                () -> assertEquals(EmployeeTypeEnum.FULL_TIME, result.getEmployeeType()),
                () -> assertEquals(startDate, result.getStartDate()),
                () -> assertEquals(endDate, result.getEndDate()),
                () -> assertEquals("ID123456", result.getIdentificationNumber()),
                () -> assertEquals(priorExperience, result.getPriorExperience()),
                () -> assertEquals(EmployeePositionEnum.BE_DEV, result.getPosition()),
                () -> assertEquals(EmployeeInfoStatusEnum.ACTIVE, result.getInfoStatus()));
    }

    @Test
    void toDto_shouldMapNullOptionalFields() {
        UUID infoUuid = UUID.randomUUID();
        UUID employeeUuid = UUID.randomUUID();

        EmployeeInfo entity = mockEmployeeInfo(
                infoUuid,
                employeeUuid,
                LocalDate.of(1995, 5, 10),
                EmployeeTypeEnum.PART_TIME,
                LocalDate.of(2020, 1, 15),
                null,
                "ID123456",
                null,
                EmployeePositionEnum.QA,
                EmployeeInfoStatusEnum.ACTIVE);

        EmployeeInfoDto result = mapper.toDto(entity);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(infoUuid.toString(), result.getId()),
                () -> assertEquals(employeeUuid.toString(), result.getEmployeeId()),
                () -> assertNull(result.getEndDate()),
                () -> assertNull(result.getPriorExperience()),
                () -> assertEquals(EmployeeTypeEnum.PART_TIME, result.getEmployeeType()),
                () -> assertEquals(EmployeePositionEnum.QA, result.getPosition()),
                () -> assertEquals(EmployeeInfoStatusEnum.ACTIVE, result.getInfoStatus()));
    }

    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        EmployeeInfoDto result = mapper.toDto(null);

        assertNull(result);
    }

    @Test
    void toEntity_shouldMapAllFields() {
        UUID infoUuid = UUID.randomUUID();
        UUID employeeUuid = UUID.randomUUID();

        LocalDate dateOfBirth = LocalDate.of(1990, 3, 20);
        LocalDate startDate = LocalDate.of(2018, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 31);
        Period priorExperience = Period.ofYears(5);

        EmployeeInfoDto dto = mockEmployeeInfoDto(
                infoUuid,
                employeeUuid,
                dateOfBirth,
                EmployeeTypeEnum.EXTERNAL,
                startDate,
                endDate,
                "ID987654",
                priorExperience,
                EmployeePositionEnum.HR,
                EmployeeInfoStatusEnum.INACTIVE);

        EmployeeInfo result = mapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(infoUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals(dateOfBirth, result.getDateOfBirth()),
                () -> assertEquals(EmployeeTypeEnum.EXTERNAL, result.getEmployeeType()),
                () -> assertEquals(startDate, result.getStartDate()),
                () -> assertEquals(endDate, result.getEndDate()),
                () -> assertEquals("ID987654", result.getIdentificationNumber()),
                () -> assertEquals(priorExperience, result.getPriorExperience()),
                () -> assertEquals(EmployeePositionEnum.HR, result.getPosition()),
                () -> assertEquals(EmployeeInfoStatusEnum.INACTIVE, result.getInfoStatus()));
    }

    @Test
    void toEntity_shouldMapNullOptionalFields() {
        UUID infoUuid = UUID.randomUUID();
        UUID employeeUuid = UUID.randomUUID();

        EmployeeInfoDto dto = mockEmployeeInfoDto(
                infoUuid,
                employeeUuid,
                LocalDate.of(1998, 7, 12),
                EmployeeTypeEnum.INTERN,
                LocalDate.of(2024, 2, 1),
                null,
                "ID111111",
                null,
                EmployeePositionEnum.FE_DEV,
                EmployeeInfoStatusEnum.ACTIVE);

        EmployeeInfo result = mapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(infoUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertNull(result.getEndDate()),
                () -> assertNull(result.getPriorExperience()),
                () -> assertEquals(EmployeeInfoStatusEnum.ACTIVE, result.getInfoStatus()));
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        EmployeeInfo result = mapper.toEntity(null);

        assertNull(result);
    }

    @Test
    void toEntityCreate_shouldMapAllAllowedFields() {
        UUID employeeUuid = UUID.randomUUID();

        LocalDate dateOfBirth = LocalDate.of(1994, 4, 15);
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2026, 3, 1);
        Period priorExperience = Period.ofYears(4);

        CreateEmployeeInfoDto dto = mockCreateEmployeeInfoDto(
                employeeUuid,
                dateOfBirth,
                EmployeeTypeEnum.FULL_TIME,
                startDate,
                endDate,
                "ID222222",
                priorExperience,
                EmployeePositionEnum.CEO);

        EmployeeInfo result = mapper.toEntityCreate(dto);

        assertNotNull(result);

        assertAll(
                () -> assertNull(result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals(dateOfBirth, result.getDateOfBirth()),
                () -> assertEquals(EmployeeTypeEnum.FULL_TIME, result.getEmployeeType()),
                () -> assertEquals(startDate, result.getStartDate()),
                () -> assertEquals(endDate, result.getEndDate()),
                () -> assertEquals("ID222222", result.getIdentificationNumber()),
                () -> assertEquals(priorExperience, result.getPriorExperience()),
                () -> assertEquals(EmployeePositionEnum.CEO, result.getPosition()),
                // Status is not provided on create and is ignored by the mapper.
                // It will be set to ACTIVE by the database.
                () -> assertNull(result.getInfoStatus()));
    }

    @Test
    void toEntityCreate_shouldMapWithoutOptionalFields() {
        UUID employeeUuid = UUID.randomUUID();

        CreateEmployeeInfoDto dto = mockCreateEmployeeInfoDto(
                employeeUuid,
                LocalDate.of(1999, 8, 20),
                EmployeeTypeEnum.PART_TIME,
                LocalDate.of(2025, 1, 1),
                null,
                "ID333333",
                null,
                EmployeePositionEnum.QA);

        EmployeeInfo result = mapper.toEntityCreate(dto);

        assertNotNull(result);

        assertAll(
                () -> assertNull(result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals(LocalDate.of(1999, 8, 20), result.getDateOfBirth()),
                () -> assertEquals(EmployeeTypeEnum.PART_TIME, result.getEmployeeType()),
                () -> assertEquals(LocalDate.of(2025, 1, 1), result.getStartDate()),
                () -> assertEquals("ID333333", result.getIdentificationNumber()),
                () -> assertEquals(EmployeePositionEnum.QA, result.getPosition()),
                () -> assertNull(result.getEndDate()),
                () -> assertNull(result.getPriorExperience()),
                // Status is not part of the Create DTO.
                () -> assertNull(result.getInfoStatus()));
    }

    @Test
    void toEntityCreate_shouldReturnNullWhenDtoIsNull() {
        EmployeeInfo result = mapper.toEntityCreate(null);

        assertNull(result);
    }

    @Test
    void toEntityCreateFromEmployee_shouldMapAllInfoFields() {
        LocalDate dateOfBirth = LocalDate.of(1993, 9, 25);
        LocalDate startDate = LocalDate.of(2019, 4, 1);
        LocalDate endDate = LocalDate.of(2027, 4, 1);
        Period priorExperience = Period.ofYears(6);

        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setDateOfBirth(dateOfBirth);
        dto.setEmployeeType(EmployeeTypeEnum.FULL_TIME);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setIdentificationNumber("ID444444");
        dto.setPriorExperience(priorExperience);
        dto.setPosition(EmployeePositionEnum.FE_DEV);

        EmployeeInfo result = mapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(dateOfBirth, result.getDateOfBirth()),
                () -> assertEquals(EmployeeTypeEnum.FULL_TIME, result.getEmployeeType()),
                () -> assertEquals(startDate, result.getStartDate()),
                () -> assertEquals(endDate, result.getEndDate()),
                () -> assertEquals("ID444444", result.getIdentificationNumber()),
                () -> assertEquals(priorExperience, result.getPriorExperience()),
                () -> assertEquals(EmployeePositionEnum.FE_DEV, result.getPosition()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getInfoStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldIgnoreEmployeeFields() {
        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPersonalId("123456789");
        dto.setGender(EmployeeGenderEnum.MALE);
        dto.setNationality("Macedonian");
        dto.setUserId(UUID.randomUUID());

        dto.setDateOfBirth(LocalDate.of(1995, 1, 1));
        dto.setEmployeeType(EmployeeTypeEnum.EXTERNAL);
        dto.setStartDate(LocalDate.of(2022, 1, 1));
        dto.setIdentificationNumber("ID555555");
        dto.setPosition(EmployeePositionEnum.HR);

        EmployeeInfo result = mapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getInfoStatus()),
                () -> assertEquals(LocalDate.of(1995, 1, 1), result.getDateOfBirth()),
                () -> assertEquals(EmployeeTypeEnum.EXTERNAL, result.getEmployeeType()),
                () -> assertEquals(LocalDate.of(2022, 1, 1), result.getStartDate()),
                () -> assertEquals("ID555555", result.getIdentificationNumber()),
                () -> assertEquals(EmployeePositionEnum.HR, result.getPosition()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldMapOptionalFieldsWhenPresent() {
        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setDateOfBirth(LocalDate.of(1990, 10, 10));
        dto.setEmployeeType(EmployeeTypeEnum.INTERN);
        dto.setStartDate(LocalDate.of(2024, 5, 1));
        dto.setEndDate(LocalDate.of(2025, 5, 1));
        dto.setIdentificationNumber("ID666666");
        dto.setPriorExperience(Period.ofYears(2));
        dto.setPosition(EmployeePositionEnum.BE_DEV);

        EmployeeInfo result = mapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(LocalDate.of(2025, 5, 1), result.getEndDate()),
                () -> assertEquals(Period.ofYears(2), result.getPriorExperience()),
                () -> assertNull(result.getInfoStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldLeaveOptionalFieldsNullWhenNotProvided() {
        CreateEmployeeDto dto = new CreateEmployeeDto();

        dto.setDateOfBirth(LocalDate.of(2000, 2, 20));
        dto.setEmployeeType(EmployeeTypeEnum.PART_TIME);
        dto.setStartDate(LocalDate.of(2025, 6, 1));
        dto.setIdentificationNumber("ID777777");
        dto.setPosition(EmployeePositionEnum.QA);

        EmployeeInfo result = mapper.toEntityCreateFromEmployee(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getEndDate()),
                () -> assertNull(result.getPriorExperience()),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertNull(result.getInfoStatus()));
    }

    @Test
    void toEntityCreateFromEmployee_shouldReturnNullWhenDtoIsNull() {
        EmployeeInfo result = mapper.toEntityCreateFromEmployee(null);

        assertNull(result);
    }

    @Test
    void toEntityUpdate_shouldMapAllAllowedFields() {
        LocalDate dateOfBirth = LocalDate.of(1988, 11, 5);
        LocalDate startDate = LocalDate.of(2017, 2, 1);
        LocalDate endDate = LocalDate.of(2027, 2, 1);
        Period priorExperience = Period.ofYears(8);

        UpdateEmployeeInfoDto dto = mockUpdateEmployeeInfoDto(
                dateOfBirth,
                EmployeeTypeEnum.EXTERNAL,
                startDate,
                endDate,
                "ID888888",
                priorExperience,
                EmployeePositionEnum.CEO,
                EmployeeInfoStatusEnum.INACTIVE);

        EmployeeInfo result = mapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getEmployeeUuid()),
                () -> assertEquals(dateOfBirth, result.getDateOfBirth()),
                () -> assertEquals(EmployeeTypeEnum.EXTERNAL, result.getEmployeeType()),
                () -> assertEquals(startDate, result.getStartDate()),
                () -> assertEquals(endDate, result.getEndDate()),
                () -> assertEquals("ID888888", result.getIdentificationNumber()),
                () -> assertEquals(priorExperience, result.getPriorExperience()),
                () -> assertEquals(EmployeePositionEnum.CEO, result.getPosition()),
                () -> assertEquals(EmployeeInfoStatusEnum.INACTIVE, result.getInfoStatus()));
    }

    @Test
    void toEntityUpdate_shouldMapNullOptionalEndDate() {
        UpdateEmployeeInfoDto dto = mockUpdateEmployeeInfoDto(
                LocalDate.of(1997, 6, 15),
                EmployeeTypeEnum.FULL_TIME,
                LocalDate.of(2023, 1, 1),
                null,
                "ID999999",
                Period.ofYears(1),
                EmployeePositionEnum.FE_DEV,
                EmployeeInfoStatusEnum.ACTIVE);

        EmployeeInfo result = mapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getEndDate()),
                () -> assertEquals(Period.ofYears(1), result.getPriorExperience()),
                () -> assertEquals(EmployeeInfoStatusEnum.ACTIVE, result.getInfoStatus()));
    }

    @Test
    void toEntityUpdate_shouldReturnNullWhenDtoIsNull() {
        EmployeeInfo result = mapper.toEntityUpdate(null);

        assertNull(result);
    }

    @Test
    void toDeletedDto_shouldMapUuid() {
        UUID uuid = UUID.randomUUID();

        DeleteDto result = mapper.toDeletedDto(uuid);

        assertAll(() -> assertNotNull(result), () -> assertEquals(uuid, result.getUuid()));
    }

    @Test
    void toDeletedDto_shouldReturnNullWhenUuidIsNull() {
        DeleteDto result = mapper.toDeletedDto(null);

        assertNull(result);
    }

    @Test
    void toDtoList_shouldMapAllEntities() {
        UUID firstUuid = UUID.randomUUID();
        UUID firstEmployeeUuid = UUID.randomUUID();

        UUID secondUuid = UUID.randomUUID();
        UUID secondEmployeeUuid = UUID.randomUUID();

        EmployeeInfo first = mockEmployeeInfo(
                firstUuid,
                firstEmployeeUuid,
                LocalDate.of(1990, 1, 1),
                EmployeeTypeEnum.FULL_TIME,
                LocalDate.of(2020, 1, 1),
                null,
                "ID111111",
                Period.ofYears(2),
                EmployeePositionEnum.FE_DEV,
                EmployeeInfoStatusEnum.ACTIVE);

        EmployeeInfo second = mockEmployeeInfo(
                secondUuid,
                secondEmployeeUuid,
                LocalDate.of(1995, 5, 5),
                EmployeeTypeEnum.PART_TIME,
                LocalDate.of(2022, 5, 1),
                LocalDate.of(2026, 5, 1),
                "ID222222",
                null,
                EmployeePositionEnum.QA,
                EmployeeInfoStatusEnum.INACTIVE);

        List<EmployeeInfoDto> result = mapper.toDtoList(List.of(first, second));

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals(firstUuid.toString(), result.getFirst().getId()),
                () -> assertEquals(
                        firstEmployeeUuid.toString(), result.getFirst().getEmployeeId()),
                () -> assertEquals(EmployeeTypeEnum.FULL_TIME, result.getFirst().getEmployeeType()),
                () -> assertEquals(
                        EmployeePositionEnum.FE_DEV, result.getFirst().getPosition()),
                () -> assertEquals(
                        EmployeeInfoStatusEnum.ACTIVE, result.getFirst().getInfoStatus()),
                () -> assertEquals(secondUuid.toString(), result.get(1).getId()),
                () -> assertEquals(secondEmployeeUuid.toString(), result.get(1).getEmployeeId()),
                () -> assertEquals(EmployeeTypeEnum.PART_TIME, result.get(1).getEmployeeType()),
                () -> assertEquals(EmployeePositionEnum.QA, result.get(1).getPosition()),
                () -> assertEquals(
                        EmployeeInfoStatusEnum.INACTIVE, result.get(1).getInfoStatus()));
    }

    @Test
    void toDtoList_shouldReturnEmptyListWhenInputIsEmpty() {
        List<EmployeeInfoDto> result = mapper.toDtoList(List.of());

        assertAll(() -> assertNotNull(result), () -> assertTrue(result.isEmpty()));
    }

    @Test
    void toDtoList_shouldReturnNullWhenInputIsNull() {
        List<EmployeeInfoDto> result = mapper.toDtoList(null);

        assertNull(result);
    }

    private EmployeeInfo mockEmployeeInfo(
            UUID uuid,
            UUID employeeUuid,
            LocalDate dateOfBirth,
            EmployeeTypeEnum employeeType,
            LocalDate startDate,
            LocalDate endDate,
            String identificationNumber,
            Period priorExperience,
            EmployeePositionEnum position,
            EmployeeInfoStatusEnum infoStatus) {
        return EmployeeInfo.builder()
                .uuid(uuid)
                .employeeUuid(employeeUuid)
                .dateOfBirth(dateOfBirth)
                .employeeType(employeeType)
                .startDate(startDate)
                .endDate(endDate)
                .identificationNumber(identificationNumber)
                .priorExperience(priorExperience)
                .position(position)
                .infoStatus(infoStatus)
                .build();
    }

    private EmployeeInfoDto mockEmployeeInfoDto(
            UUID uuid,
            UUID employeeUuid,
            LocalDate dateOfBirth,
            EmployeeTypeEnum employeeType,
            LocalDate startDate,
            LocalDate endDate,
            String identificationNumber,
            Period priorExperience,
            EmployeePositionEnum position,
            EmployeeInfoStatusEnum infoStatus) {
        return EmployeeInfoDto.builder()
                .id(uuid.toString())
                .employeeId(employeeUuid.toString())
                .dateOfBirth(dateOfBirth)
                .employeeType(employeeType)
                .startDate(startDate)
                .endDate(endDate)
                .identificationNumber(identificationNumber)
                .priorExperience(priorExperience)
                .position(position)
                .infoStatus(infoStatus)
                .build();
    }

    private CreateEmployeeInfoDto mockCreateEmployeeInfoDto(
            UUID employeeId,
            LocalDate dateOfBirth,
            EmployeeTypeEnum employeeType,
            LocalDate startDate,
            LocalDate endDate,
            String identificationNumber,
            Period priorExperience,
            EmployeePositionEnum position) {
        return CreateEmployeeInfoDto.builder()
                .employeeId(employeeId)
                .dateOfBirth(dateOfBirth)
                .employeeType(employeeType)
                .startDate(startDate)
                .endDate(endDate)
                .identificationNumber(identificationNumber)
                .priorExperience(priorExperience)
                .position(position)
                .build();
    }

    private UpdateEmployeeInfoDto mockUpdateEmployeeInfoDto(
            LocalDate dateOfBirth,
            EmployeeTypeEnum employeeType,
            LocalDate startDate,
            LocalDate endDate,
            String identificationNumber,
            Period priorExperience,
            EmployeePositionEnum position,
            EmployeeInfoStatusEnum infoStatus) {
        return UpdateEmployeeInfoDto.builder()
                .dateOfBirth(dateOfBirth)
                .employeeType(employeeType)
                .startDate(startDate)
                .endDate(endDate)
                .identificationNumber(identificationNumber)
                .priorExperience(priorExperience)
                .position(position)
                .infoStatus(infoStatus)
                .build();
    }
}
