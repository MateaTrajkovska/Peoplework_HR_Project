package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.EmployeeReport;
import com.h4h.employeeportal.employee.core.service.EmployeeReportService;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.mapper.EmployeeReportMapper;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeReportRestServiceTest {

    @Mock
    private EmployeeReportService employeeReportService;

    @Mock
    private EmployeeReportMapper employeeReportMapper;

    @InjectMocks
    private EmployeeReportRestService employeeReportRestService;

    private UUID reportUuid;

    private EmployeeReport employeeReport;
    private EmployeeReport mappedEmployeeReport;
    private EmployeeReportDto employeeReportDto;
    private CreateEmployeeReportDto createEmployeeReportDto;
    private UpdateEmployeeReportDto updateEmployeeReportDto;

    @BeforeEach
    void setUp() {
        reportUuid = UUID.randomUUID();

        employeeReport = mock(EmployeeReport.class);
        mappedEmployeeReport = mock(EmployeeReport.class);
        employeeReportDto = mock(EmployeeReportDto.class);
        createEmployeeReportDto = mock(CreateEmployeeReportDto.class);
        updateEmployeeReportDto = mock(UpdateEmployeeReportDto.class);
    }

    private ResourceNotFoundException notFoundException() {
        return new ResourceNotFoundException("employee.report.notFound");
    }

    @Test
    void createEmployeeReport_shouldMapSaveAndReturnDto() {
        when(employeeReportMapper.toEntityCreate(createEmployeeReportDto)).thenReturn(mappedEmployeeReport);

        when(employeeReportService.createEmployeeReport(mappedEmployeeReport)).thenReturn(employeeReport);

        when(employeeReportMapper.toDto(employeeReport)).thenReturn(employeeReportDto);

        EmployeeReportDto result = employeeReportRestService.createEmployeeReport(createEmployeeReportDto);

        assertAll(() -> assertNotNull(result), () -> assertSame(employeeReportDto, result));

        verify(employeeReportMapper).toEntityCreate(createEmployeeReportDto);

        verify(employeeReportService).createEmployeeReport(mappedEmployeeReport);

        verify(employeeReportMapper).toDto(employeeReport);
    }

    @Test
    void createEmployeeReport_shouldReturnNullWhenServiceReturnsNull() {
        when(employeeReportMapper.toEntityCreate(createEmployeeReportDto)).thenReturn(mappedEmployeeReport);

        when(employeeReportService.createEmployeeReport(mappedEmployeeReport)).thenReturn(null);

        when(employeeReportMapper.toDto(null)).thenReturn(null);

        EmployeeReportDto result = employeeReportRestService.createEmployeeReport(createEmployeeReportDto);

        assertNull(result);

        verify(employeeReportMapper).toEntityCreate(createEmployeeReportDto);

        verify(employeeReportService).createEmployeeReport(mappedEmployeeReport);

        verify(employeeReportMapper).toDto(null);
    }

    @Test
    void createEmployeeReport_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Mapping failed");

        when(employeeReportMapper.toEntityCreate(createEmployeeReportDto)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class, () -> employeeReportRestService.createEmployeeReport(createEmployeeReportDto));

        assertSame(exception, thrown);

        verify(employeeReportMapper).toEntityCreate(createEmployeeReportDto);

        verifyNoInteractions(employeeReportService);
    }

    @Test
    void createEmployeeReport_shouldPropagateServiceException() {
        when(employeeReportMapper.toEntityCreate(createEmployeeReportDto)).thenReturn(mappedEmployeeReport);

        RuntimeException exception = new RuntimeException("Create failed");

        when(employeeReportService.createEmployeeReport(mappedEmployeeReport)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class, () -> employeeReportRestService.createEmployeeReport(createEmployeeReportDto));

        assertSame(exception, thrown);

        verify(employeeReportMapper).toEntityCreate(createEmployeeReportDto);

        verify(employeeReportService).createEmployeeReport(mappedEmployeeReport);

        verify(employeeReportMapper, never()).toDto(any());
    }

    @Test
    void getAllEmployeeReports_shouldReturnMappedList() {
        EmployeeReport secondReport = mock(EmployeeReport.class);
        EmployeeReportDto secondDto = mock(EmployeeReportDto.class);

        List<EmployeeReport> reports = List.of(employeeReport, secondReport);

        List<EmployeeReportDto> expectedDtos = List.of(employeeReportDto, secondDto);

        when(employeeReportService.getAllEmployeeReports()).thenReturn(reports);

        when(employeeReportMapper.toDtoList(reports)).thenReturn(expectedDtos);

        List<EmployeeReportDto> result = employeeReportRestService.getAllEmployeeReports();

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertSame(expectedDtos, result));

        verify(employeeReportService).getAllEmployeeReports();

        verify(employeeReportMapper).toDtoList(reports);
    }

    @Test
    void getAllEmployeeReports_shouldReturnEmptyList() {
        List<EmployeeReport> reports = List.of();
        List<EmployeeReportDto> expectedDtos = List.of();

        when(employeeReportService.getAllEmployeeReports()).thenReturn(reports);

        when(employeeReportMapper.toDtoList(reports)).thenReturn(expectedDtos);

        List<EmployeeReportDto> result = employeeReportRestService.getAllEmployeeReports();

        assertTrue(result.isEmpty());

        verify(employeeReportService).getAllEmployeeReports();

        verify(employeeReportMapper).toDtoList(reports);
    }

    @Test
    void getAllEmployeeReports_shouldPropagateServiceException() {
        RuntimeException exception = new RuntimeException("Database error");

        when(employeeReportService.getAllEmployeeReports()).thenThrow(exception);

        RuntimeException thrown =
                assertThrows(RuntimeException.class, () -> employeeReportRestService.getAllEmployeeReports());

        assertSame(exception, thrown);

        verify(employeeReportService).getAllEmployeeReports();

        verifyNoInteractions(employeeReportMapper);
    }

    @Test
    void getEmployeeReportById_shouldReturnMappedDto() {
        when(employeeReportService.getEmployeeReportById(reportUuid)).thenReturn(employeeReport);

        when(employeeReportMapper.toDto(employeeReport)).thenReturn(employeeReportDto);

        EmployeeReportDto result = employeeReportRestService.getEmployeeReportById(reportUuid);

        assertAll(() -> assertNotNull(result), () -> assertSame(employeeReportDto, result));

        verify(employeeReportService).getEmployeeReportById(reportUuid);

        verify(employeeReportMapper).toDto(employeeReport);
    }

    @Test
    void getEmployeeReportById_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeReportService.getEmployeeReportById(reportUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeReportRestService.getEmployeeReportById(reportUuid));

        assertSame(exception, thrown);

        verify(employeeReportService).getEmployeeReportById(reportUuid);

        verifyNoInteractions(employeeReportMapper);
    }

    @Test
    void updateEmployeeReport_shouldMapUpdateAndReturnDto() {
        when(employeeReportMapper.toEntityUpdate(updateEmployeeReportDto)).thenReturn(mappedEmployeeReport);

        when(employeeReportService.updateEmployeeReport(reportUuid, mappedEmployeeReport))
                .thenReturn(employeeReport);

        when(employeeReportMapper.toDto(employeeReport)).thenReturn(employeeReportDto);

        EmployeeReportDto result = employeeReportRestService.updateEmployeeReport(reportUuid, updateEmployeeReportDto);

        assertAll(() -> assertNotNull(result), () -> assertSame(employeeReportDto, result));

        verify(employeeReportMapper).toEntityUpdate(updateEmployeeReportDto);

        verify(employeeReportService).updateEmployeeReport(reportUuid, mappedEmployeeReport);

        verify(employeeReportMapper).toDto(employeeReport);
    }

    @Test
    void updateEmployeeReport_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Mapping failed");

        when(employeeReportMapper.toEntityUpdate(updateEmployeeReportDto)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeReportRestService.updateEmployeeReport(reportUuid, updateEmployeeReportDto));

        assertSame(exception, thrown);

        verify(employeeReportMapper).toEntityUpdate(updateEmployeeReportDto);

        verifyNoInteractions(employeeReportService);
    }

    @Test
    void updateEmployeeReport_shouldPropagateNotFoundException() {
        when(employeeReportMapper.toEntityUpdate(updateEmployeeReportDto)).thenReturn(mappedEmployeeReport);

        ResourceNotFoundException exception = notFoundException();

        when(employeeReportService.updateEmployeeReport(reportUuid, mappedEmployeeReport))
                .thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeReportRestService.updateEmployeeReport(reportUuid, updateEmployeeReportDto));

        assertSame(exception, thrown);

        verify(employeeReportMapper).toEntityUpdate(updateEmployeeReportDto);

        verify(employeeReportService).updateEmployeeReport(reportUuid, mappedEmployeeReport);

        verify(employeeReportMapper, never()).toDto(any());
    }

    @Test
    void deleteEmployeeReport_shouldDeleteAndReturnDto() {
        DeleteDto expectedDto = new DeleteDto();
        expectedDto.setUuid(reportUuid);

        when(employeeReportService.deleteEmployeeReportById(reportUuid)).thenReturn(reportUuid);

        when(employeeReportMapper.toDeletedDto(reportUuid)).thenReturn(expectedDto);

        DeleteDto result = employeeReportRestService.deleteEmployeeReport(reportUuid);

        assertAll(
                () -> assertNotNull(result),
                () -> assertSame(expectedDto, result),
                () -> assertEquals(reportUuid, result.getUuid()));

        verify(employeeReportService).deleteEmployeeReportById(reportUuid);

        verify(employeeReportMapper).toDeletedDto(reportUuid);
    }

    @Test
    void deleteEmployeeReport_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeReportService.deleteEmployeeReportById(reportUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeReportRestService.deleteEmployeeReport(reportUuid));

        assertSame(exception, thrown);

        verify(employeeReportService).deleteEmployeeReportById(reportUuid);

        verifyNoInteractions(employeeReportMapper);
    }

    @Test
    void deleteEmployeeReport_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Delete mapping failed");

        when(employeeReportService.deleteEmployeeReportById(reportUuid)).thenReturn(reportUuid);

        when(employeeReportMapper.toDeletedDto(reportUuid)).thenThrow(exception);

        RuntimeException thrown =
                assertThrows(RuntimeException.class, () -> employeeReportRestService.deleteEmployeeReport(reportUuid));

        assertSame(exception, thrown);

        verify(employeeReportService).deleteEmployeeReportById(reportUuid);

        verify(employeeReportMapper).toDeletedDto(reportUuid);
    }
}
