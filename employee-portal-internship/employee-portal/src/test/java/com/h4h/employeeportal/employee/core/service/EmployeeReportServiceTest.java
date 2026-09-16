package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeReportStatusEnum;
import com.h4h.employeeportal.employee.core.model.EmployeeReport;
import com.h4h.employeeportal.employee.core.model.QEmployeeReport;
import com.h4h.employeeportal.employee.core.repository.EmployeeReportRepository;
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
class EmployeeReportServiceTest {

    @Mock
    private EmployeeReportRepository employeeReportRepository;

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAUpdateClause updateClause;

    @InjectMocks
    private EmployeeReportService employeeReportService;

    private UUID reportUuid;
    private UUID employeeUuid;
    private EmployeeReport employeeReport;

    @BeforeEach
    void setUp() {
        reportUuid = UUID.randomUUID();
        employeeUuid = UUID.randomUUID();

        employeeReport = mockEmployeeReport(
                reportUuid, employeeUuid, "Employee performance is satisfactory.", EmployeeReportStatusEnum.ACTIVE);
    }

    private EmployeeReport mockEmployeeReport(
            UUID uuid, UUID employeeUuid, String internalNote, EmployeeReportStatusEnum reportStatus) {

        return EmployeeReport.builder()
                .uuid(uuid)
                .employeeUuid(employeeUuid)
                .internalNote(internalNote)
                .reportStatus(reportStatus)
                .build();
    }

    private void mockDeleteQuery(long affectedRows) {
        when(queryFactory.update(QEmployeeReport.employeeReport)).thenReturn(updateClause);

        when(updateClause.where(any(Predicate.class))).thenReturn(updateClause);

        when(updateClause.set(QEmployeeReport.employeeReport.reportStatus, EmployeeReportStatusEnum.INACTIVE))
                .thenReturn(updateClause);

        when(updateClause.execute()).thenReturn(affectedRows);
    }

    private void verifyDeleteQuery() {
        verify(queryFactory).update(QEmployeeReport.employeeReport);
        verify(updateClause).where(any(Predicate.class));
        verify(updateClause).set(QEmployeeReport.employeeReport.reportStatus, EmployeeReportStatusEnum.INACTIVE);
        verify(updateClause).execute();
    }

    @Test
    void createEmployeeReport_shouldSaveAndReturnReportWithNote() {
        when(employeeReportRepository.save(employeeReport)).thenReturn(employeeReport);

        EmployeeReport result = employeeReportService.createEmployeeReport(employeeReport);

        assertAll(
                () -> assertSame(employeeReport, result),
                () -> assertEquals(reportUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals("Employee performance is satisfactory.", result.getInternalNote()),
                () -> assertEquals(EmployeeReportStatusEnum.ACTIVE, result.getReportStatus()));

        verify(employeeReportRepository).save(employeeReport);
    }

    @Test
    void createEmployeeReport_shouldSaveAndReturnReportWithoutOptionalNote() {
        EmployeeReport reportWithoutNote = mockEmployeeReport(null, employeeUuid, null, null);

        EmployeeReport savedReport =
                mockEmployeeReport(reportUuid, employeeUuid, null, EmployeeReportStatusEnum.ACTIVE);

        when(employeeReportRepository.save(reportWithoutNote)).thenReturn(savedReport);

        EmployeeReport result = employeeReportService.createEmployeeReport(reportWithoutNote);

        assertAll(
                () -> assertSame(savedReport, result),
                () -> assertEquals(reportUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertNull(result.getInternalNote()),
                () -> assertEquals(EmployeeReportStatusEnum.ACTIVE, result.getReportStatus()));

        verify(employeeReportRepository).save(reportWithoutNote);
    }

    @Test
    void createEmployeeReport_shouldSaveNoteWithMaximumAllowedLength() {
        String maximumLengthNote = "a".repeat(1000);

        EmployeeReport report = mockEmployeeReport(null, employeeUuid, maximumLengthNote, null);

        EmployeeReport savedReport =
                mockEmployeeReport(reportUuid, employeeUuid, maximumLengthNote, EmployeeReportStatusEnum.ACTIVE);

        when(employeeReportRepository.save(report)).thenReturn(savedReport);

        EmployeeReport result = employeeReportService.createEmployeeReport(report);

        assertAll(
                () -> assertSame(savedReport, result),
                () -> assertEquals(1000, result.getInternalNote().length()),
                () -> assertEquals(maximumLengthNote, result.getInternalNote()));

        verify(employeeReportRepository).save(report);
    }

    @Test
    void getEmployeeReportById_shouldReturnReportWhenFound() {
        when(employeeReportRepository.findById(reportUuid)).thenReturn(Optional.of(employeeReport));

        EmployeeReport result = employeeReportService.getEmployeeReportById(reportUuid);

        assertSame(employeeReport, result);

        verify(employeeReportRepository).findById(reportUuid);
    }

    @Test
    void getEmployeeReportById_shouldThrowExceptionWhenReportNotFound() {
        when(employeeReportRepository.findById(reportUuid)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeReportService.getEmployeeReportById(reportUuid));

        assertEquals("employee.report.notFound", exception.getMessage());

        verify(employeeReportRepository).findById(reportUuid);
    }

    @Test
    void getAllEmployeeReports_shouldReturnAllReports() {
        EmployeeReport secondReport = mockEmployeeReport(
                UUID.randomUUID(), UUID.randomUUID(), "Second employee report.", EmployeeReportStatusEnum.ACTIVE);

        List<EmployeeReport> reports = List.of(employeeReport, secondReport);

        when(employeeReportRepository.findAll()).thenReturn(reports);

        List<EmployeeReport> result = employeeReportService.getAllEmployeeReports();

        assertEquals(reports, result);
        assertEquals(2, result.size());

        verify(employeeReportRepository).findAll();
    }

    @Test
    void getAllEmployeeReports_shouldReturnEmptyListWhenNoReportsExist() {
        when(employeeReportRepository.findAll()).thenReturn(List.of());

        List<EmployeeReport> result = employeeReportService.getAllEmployeeReports();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employeeReportRepository).findAll();
    }

    @Test
    void getEmployeeReportByEmployeeUuid_shouldReturnReportWhenFound() {
        when(employeeReportRepository.findEmployeeReportByEmployeeUuid(employeeUuid))
                .thenReturn(Optional.of(employeeReport));

        EmployeeReport result = employeeReportService.getEmployeeReportByEmployeeUuid(employeeUuid);

        assertSame(employeeReport, result);

        verify(employeeReportRepository).findEmployeeReportByEmployeeUuid(employeeUuid);
    }

    @Test
    void getEmployeeReportByEmployeeUuid_shouldThrowExceptionWhenReportNotFound() {
        when(employeeReportRepository.findEmployeeReportByEmployeeUuid(employeeUuid))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeReportService.getEmployeeReportByEmployeeUuid(employeeUuid));

        assertEquals("employee.report.notFound", exception.getMessage());

        verify(employeeReportRepository).findEmployeeReportByEmployeeUuid(employeeUuid);
    }

    @Test
    void updateEmployeeReport_shouldUpdateNoteAndPreserveIdentifiers() {
        EmployeeReport updatedReport = mockEmployeeReport(
                UUID.randomUUID(), UUID.randomUUID(), "Updated internal note.", EmployeeReportStatusEnum.INACTIVE);

        when(employeeReportRepository.findById(reportUuid)).thenReturn(Optional.of(employeeReport));

        when(employeeReportRepository.save(employeeReport)).thenReturn(employeeReport);

        EmployeeReport result = employeeReportService.updateEmployeeReport(reportUuid, updatedReport);

        assertAll(
                () -> assertSame(employeeReport, result),
                () -> assertEquals(reportUuid, result.getUuid()),
                () -> assertEquals(employeeUuid, result.getEmployeeUuid()),
                () -> assertEquals("Updated internal note.", result.getInternalNote()),
                () -> assertEquals(EmployeeReportStatusEnum.INACTIVE, result.getReportStatus()));

        verify(employeeReportRepository).findById(reportUuid);
        verify(employeeReportRepository).save(employeeReport);
    }

    @Test
    void updateEmployeeReport_shouldClearOptionalNoteWhenNull() {
        EmployeeReport updatedReport =
                mockEmployeeReport(UUID.randomUUID(), UUID.randomUUID(), null, EmployeeReportStatusEnum.ACTIVE);

        when(employeeReportRepository.findById(reportUuid)).thenReturn(Optional.of(employeeReport));

        when(employeeReportRepository.save(employeeReport)).thenReturn(employeeReport);

        EmployeeReport result = employeeReportService.updateEmployeeReport(reportUuid, updatedReport);

        assertNull(result.getInternalNote());

        verify(employeeReportRepository).findById(reportUuid);
        verify(employeeReportRepository).save(employeeReport);
    }

    @Test
    void updateEmployeeReport_shouldSetOptionalNoteWhenPreviouslyNull() {
        EmployeeReport existingReport =
                mockEmployeeReport(reportUuid, employeeUuid, null, EmployeeReportStatusEnum.ACTIVE);

        EmployeeReport updatedReport = mockEmployeeReport(
                UUID.randomUUID(), UUID.randomUUID(), "New internal note.", EmployeeReportStatusEnum.ACTIVE);

        when(employeeReportRepository.findById(reportUuid)).thenReturn(Optional.of(existingReport));

        when(employeeReportRepository.save(existingReport)).thenReturn(existingReport);

        EmployeeReport result = employeeReportService.updateEmployeeReport(reportUuid, updatedReport);

        assertEquals("New internal note.", result.getInternalNote());

        verify(employeeReportRepository).findById(reportUuid);
        verify(employeeReportRepository).save(existingReport);
    }

    @Test
    void updateEmployeeReport_shouldThrowExceptionWhenReportNotFound() {
        when(employeeReportRepository.findById(reportUuid)).thenReturn(Optional.empty());

        EmployeeReport updatedReport =
                mockEmployeeReport(UUID.randomUUID(), employeeUuid, "Updated note.", EmployeeReportStatusEnum.ACTIVE);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeReportService.updateEmployeeReport(reportUuid, updatedReport));

        assertEquals("employee.report.notFound", exception.getMessage());

        verify(employeeReportRepository).findById(reportUuid);
        verify(employeeReportRepository, never()).save(any());
    }

    @Test
    void deleteEmployeeReportById_shouldReturnUuidWhenReportExists() {
        mockDeleteQuery(1L);

        UUID result = employeeReportService.deleteEmployeeReportById(reportUuid);

        assertEquals(reportUuid, result);

        verifyDeleteQuery();
    }

    @Test
    void deleteEmployeeReportById_shouldThrowExceptionWhenNoRowsUpdated() {
        mockDeleteQuery(0L);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> employeeReportService.deleteEmployeeReportById(reportUuid));

        assertEquals("employee.report.notFound", exception.getMessage());

        verifyDeleteQuery();
    }

    @Test
    void deleteEmployeeReportById_shouldReturnUuidWhenMultipleRowsAreUpdated() {
        mockDeleteQuery(2L);

        UUID result = employeeReportService.deleteEmployeeReportById(reportUuid);

        assertEquals(reportUuid, result);

        verifyDeleteQuery();
    }
}
