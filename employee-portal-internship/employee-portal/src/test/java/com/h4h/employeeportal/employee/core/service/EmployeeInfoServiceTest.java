package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeInfoStatusEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeePositionEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeTypeEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeInfo;
import com.h4h.employeeportal.employee.core.model.QEmployeeInfo;
import com.h4h.employeeportal.employee.core.repository.EmployeeInfoRepository;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeInfoServiceTest {

    @Mock
    private EmployeeInfoRepository employeeInfoRepository;

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAUpdateClause updateClause;

    @InjectMocks
    private EmployeeInfoService employeeInfoService;

    private UUID infoUuid;
    private UUID employeeUuid;
    private EmployeeInfo employeeInfo;

    @BeforeEach
    void setUp() {
        infoUuid = UUID.randomUUID();
        employeeUuid = UUID.randomUUID();

        employeeInfo = createEmployeeInfo(
                infoUuid,
                employeeUuid,
                LocalDate.of(1995, 5, 15),
                EmployeeTypeEnum.FULL_TIME,
                LocalDate.of(2020, 1, 10),
                LocalDate.of(2025, 12, 31),
                "ID123456",
                Period.ofYears(2),
                EmployeePositionEnum.BE_DEV,
                EmployeeInfoStatusEnum.ACTIVE);
    }

    private EmployeeInfo createEmployeeInfo(
            UUID uuid,
            UUID employeeUuid,
            LocalDate dateOfBirth,
            EmployeeTypeEnum employeeType,
            LocalDate startDate,
            LocalDate endDate,
            String identificationNumber,
            Period priorExperience,
            EmployeePositionEnum position,
            EmployeeInfoStatusEnum status) {
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
                .infoStatus(status)
                .build();
    }

    private EmployeeInfo createUpdatedEmployeeInfo(
            LocalDate dateOfBirth,
            LocalDate startDate,
            LocalDate endDate,
            String identificationNumber,
            Period priorExperience,
            EmployeePositionEnum position,
            EmployeeInfoStatusEnum status) {
        return createEmployeeInfo(
                UUID.randomUUID(),
                UUID.randomUUID(),
                dateOfBirth,
                EmployeeTypeEnum.FULL_TIME,
                startDate,
                endDate,
                identificationNumber,
                priorExperience,
                position,
                status);
    }

    private void mockInfoFound() {
        when(employeeInfoRepository.findById(infoUuid)).thenReturn(Optional.of(employeeInfo));
    }

    private void mockDelete(long updatedRows) {
        when(queryFactory.update(QEmployeeInfo.employeeInfo)).thenReturn(updateClause);

        when(updateClause.where(any(Predicate.class))).thenReturn(updateClause);

        when(updateClause.set(QEmployeeInfo.employeeInfo.infoStatus, EmployeeInfoStatusEnum.INACTIVE))
                .thenReturn(updateClause);

        when(updateClause.execute()).thenReturn(updatedRows);
    }

    private void verifyDelete() {
        verify(queryFactory).update(QEmployeeInfo.employeeInfo);
        verify(updateClause).where(any(Predicate.class));
        verify(updateClause).set(QEmployeeInfo.employeeInfo.infoStatus, EmployeeInfoStatusEnum.INACTIVE);
        verify(updateClause).execute();
    }

    @Test
    void createEmployeeInfo_shouldSaveAndReturnInfoWithAllFields() {
        when(employeeInfoRepository.save(employeeInfo)).thenReturn(employeeInfo);

        EmployeeInfo result = employeeInfoService.createEmployeeInfo(employeeInfo);

        assertSame(employeeInfo, result);

        assertAll(
                () -> assertEquals(infoUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals(LocalDate.of(1995, 5, 15), result.getDateOfBirth()),
                () -> assertEquals(EmployeeTypeEnum.FULL_TIME, result.getEmployeeType()),
                () -> assertEquals(LocalDate.of(2020, 1, 10), result.getStartDate()),
                () -> assertEquals(LocalDate.of(2025, 12, 31), result.getEndDate()),
                () -> assertEquals("ID123456", result.getIdentificationNumber()),
                () -> assertEquals(Period.ofYears(2), result.getPriorExperience()),
                () -> assertEquals(EmployeePositionEnum.BE_DEV, result.getPosition()),
                () -> assertEquals(EmployeeInfoStatusEnum.ACTIVE, result.getInfoStatus()));

        verify(employeeInfoRepository).save(employeeInfo);
    }

    @Test
    void createEmployeeInfo_shouldSaveAndReturnInfoWithoutOptionalFields() {
        EmployeeInfo inputInfo = createEmployeeInfo(
                null,
                employeeUuid,
                LocalDate.of(1995, 5, 15),
                EmployeeTypeEnum.FULL_TIME,
                LocalDate.of(2020, 1, 10),
                null,
                "ID123456",
                null,
                EmployeePositionEnum.BE_DEV,
                null);

        EmployeeInfo savedInfo = createEmployeeInfo(
                infoUuid,
                employeeUuid,
                LocalDate.of(1995, 5, 15),
                EmployeeTypeEnum.FULL_TIME,
                LocalDate.of(2020, 1, 10),
                null,
                "ID123456",
                null,
                EmployeePositionEnum.BE_DEV,
                EmployeeInfoStatusEnum.ACTIVE);

        when(employeeInfoRepository.save(inputInfo)).thenReturn(savedInfo);

        EmployeeInfo result = employeeInfoService.createEmployeeInfo(inputInfo);

        assertSame(savedInfo, result);

        assertAll(
                () -> assertEquals(infoUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals(LocalDate.of(1995, 5, 15), result.getDateOfBirth()),
                () -> assertEquals(EmployeeTypeEnum.FULL_TIME, result.getEmployeeType()),
                () -> assertEquals(LocalDate.of(2020, 1, 10), result.getStartDate()),
                () -> assertNull(result.getEndDate()),
                () -> assertEquals("ID123456", result.getIdentificationNumber()),
                () -> assertNull(result.getPriorExperience()),
                () -> assertEquals(EmployeePositionEnum.BE_DEV, result.getPosition()),
                () -> assertEquals(EmployeeInfoStatusEnum.ACTIVE, result.getInfoStatus()));

        verify(employeeInfoRepository).save(inputInfo);
    }

    @Test
    void getEmployeeInfoById_shouldReturnInfoWhenFound() {
        when(employeeInfoRepository.findById(infoUuid)).thenReturn(Optional.of(employeeInfo));

        EmployeeInfo result = employeeInfoService.getEmployeeInfoById(infoUuid);

        assertSame(employeeInfo, result);

        verify(employeeInfoRepository).findById(infoUuid);
    }

    @Test
    void getEmployeeInfoById_shouldThrowExceptionWhenInfoNotFound() {
        when(employeeInfoRepository.findById(infoUuid)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> employeeInfoService.getEmployeeInfoById(infoUuid));

        assertEquals("employee.info.notFound", exception.getMessage());

        verify(employeeInfoRepository).findById(infoUuid);
    }

    @Test
    void getAllEmployeeInfo_shouldReturnAllInfo() {
        EmployeeInfo secondInfo = createEmployeeInfo(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.of(1990, 3, 20),
                EmployeeTypeEnum.FULL_TIME,
                LocalDate.of(2018, 6, 1),
                null,
                "ID654321",
                Period.ofYears(5),
                EmployeePositionEnum.QA,
                EmployeeInfoStatusEnum.ACTIVE);

        List<EmployeeInfo> infos = List.of(employeeInfo, secondInfo);

        when(employeeInfoRepository.findAll()).thenReturn(infos);

        List<EmployeeInfo> result = employeeInfoService.getAllEmployeeInfo();

        assertAll(() -> assertEquals(infos, result), () -> assertEquals(2, result.size()));

        verify(employeeInfoRepository).findAll();
    }

    @Test
    void getAllEmployeeInfo_shouldReturnEmptyListWhenNoInfoExists() {
        when(employeeInfoRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeInfo> result = employeeInfoService.getAllEmployeeInfo();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employeeInfoRepository).findAll();
    }

    @Test
    void getEmployeeInfoByEmployeeUuid_shouldReturnInfoWhenFound() {
        when(employeeInfoRepository.findEmployeeInfoByEmployeeUuid(employeeUuid))
                .thenReturn(Optional.of(employeeInfo));

        EmployeeInfo result = employeeInfoService.getEmployeeInfoByEmployeeUuid(employeeUuid);

        assertSame(employeeInfo, result);

        verify(employeeInfoRepository).findEmployeeInfoByEmployeeUuid(employeeUuid);
    }

    @Test
    void getEmployeeInfoByEmployeeUuid_shouldThrowExceptionWhenInfoNotFound() {
        when(employeeInfoRepository.findEmployeeInfoByEmployeeUuid(employeeUuid))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeInfoService.getEmployeeInfoByEmployeeUuid(employeeUuid));

        assertEquals("employee.info.notFound", exception.getMessage());

        verify(employeeInfoRepository).findEmployeeInfoByEmployeeUuid(employeeUuid);
    }

    @Test
    void updateEmployeeInfo_shouldUpdatePropertiesAndPreserveIdentifiers() {
        EmployeeInfo updatedInfo = createUpdatedEmployeeInfo(
                LocalDate.of(1996, 6, 20),
                LocalDate.of(2021, 2, 15),
                LocalDate.of(2026, 12, 31),
                "UPDATED123",
                Period.ofYears(3),
                EmployeePositionEnum.FE_DEV,
                EmployeeInfoStatusEnum.INACTIVE);

        mockInfoFound();

        when(employeeInfoRepository.save(employeeInfo)).thenReturn(employeeInfo);

        EmployeeInfo result = employeeInfoService.updateEmployeeInfo(infoUuid, updatedInfo);

        assertSame(employeeInfo, result);

        assertAll(
                () -> assertEquals(infoUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals(LocalDate.of(1996, 6, 20), result.getDateOfBirth()),
                () -> assertEquals(EmployeeTypeEnum.FULL_TIME, result.getEmployeeType()),
                () -> assertEquals(LocalDate.of(2021, 2, 15), result.getStartDate()),
                () -> assertEquals(LocalDate.of(2026, 12, 31), result.getEndDate()),
                () -> assertEquals("UPDATED123", result.getIdentificationNumber()),
                () -> assertEquals(Period.ofYears(3), result.getPriorExperience()),
                () -> assertEquals(EmployeePositionEnum.FE_DEV, result.getPosition()),
                () -> assertEquals(EmployeeInfoStatusEnum.INACTIVE, result.getInfoStatus()));

        verify(employeeInfoRepository).findById(infoUuid);
        verify(employeeInfoRepository).save(employeeInfo);
    }

    @Test
    void updateEmployeeInfo_shouldUpdateOptionalFieldsWhenProvided() {
        EmployeeInfo updatedInfo = createUpdatedEmployeeInfo(
                LocalDate.of(1992, 8, 10),
                LocalDate.of(2019, 4, 1),
                LocalDate.of(2026, 4, 1),
                "UPDATED456",
                Period.ofYears(4),
                EmployeePositionEnum.HR,
                EmployeeInfoStatusEnum.ACTIVE);

        mockInfoFound();

        when(employeeInfoRepository.save(employeeInfo)).thenReturn(employeeInfo);

        EmployeeInfo result = employeeInfoService.updateEmployeeInfo(infoUuid, updatedInfo);

        assertAll(
                () -> assertEquals(LocalDate.of(2026, 4, 1), result.getEndDate()),
                () -> assertEquals(Period.ofYears(4), result.getPriorExperience()));

        verify(employeeInfoRepository).save(employeeInfo);
    }

    @Test
    void updateEmployeeInfo_shouldClearOptionalFieldsWhenNull() {
        EmployeeInfo updatedInfo = createUpdatedEmployeeInfo(
                LocalDate.of(1996, 6, 20),
                LocalDate.of(2021, 2, 15),
                null,
                "UPDATED789",
                null,
                EmployeePositionEnum.FE_DEV,
                EmployeeInfoStatusEnum.ACTIVE);

        mockInfoFound();

        when(employeeInfoRepository.save(employeeInfo)).thenReturn(employeeInfo);

        EmployeeInfo result = employeeInfoService.updateEmployeeInfo(infoUuid, updatedInfo);

        assertAll(() -> assertNull(result.getEndDate()), () -> assertNull(result.getPriorExperience()));

        verify(employeeInfoRepository).save(employeeInfo);
    }

    @Test
    void updateEmployeeInfo_shouldThrowExceptionWhenInfoNotFound() {
        when(employeeInfoRepository.findById(infoUuid)).thenReturn(Optional.empty());

        EmployeeInfo updatedInfo = createUpdatedEmployeeInfo(
                LocalDate.of(1996, 6, 20),
                LocalDate.of(2021, 2, 15),
                null,
                "UPDATED123",
                null,
                EmployeePositionEnum.FE_DEV,
                EmployeeInfoStatusEnum.ACTIVE);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeInfoService.updateEmployeeInfo(infoUuid, updatedInfo));

        assertEquals("employee.info.notFound", exception.getMessage());

        verify(employeeInfoRepository).findById(infoUuid);
        verify(employeeInfoRepository, never()).save(any());
    }

    @Test
    void deleteEmployeeInfoById_shouldReturnUuidWhenInfoExists() {
        mockDelete(1L);

        UUID result = employeeInfoService.deleteEmployeeInfoById(infoUuid);

        assertEquals(infoUuid, result);

        verifyDelete();
    }

    @Test
    void deleteEmployeeInfoById_shouldThrowExceptionWhenNoRowsUpdated() {
        mockDelete(0L);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeInfoService.deleteEmployeeInfoById(infoUuid));

        assertEquals("employee.info.notFound", exception.getMessage());

        verifyDelete();
    }

    @Test
    void deleteEmployeeInfoById_shouldReturnUuidWhenMultipleRowsAreUpdated() {
        mockDelete(2L);

        UUID result = employeeInfoService.deleteEmployeeInfoById(infoUuid);

        assertEquals(infoUuid, result);

        verifyDelete();
    }
}
