package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeReportDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeReportRestService;
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
class EmployeeReportRestControllerTest {

    @Mock
    private EmployeeReportRestService employeeReportRestService;

    @InjectMocks
    private EmployeeReportRestController employeeReportRestController;

    private UUID reportUuid;

    @BeforeEach
    void setUp() {
        reportUuid = UUID.randomUUID();
    }

    @Test
    void createEmployeeReport_shouldReturnCreatedReport() {
        CreateEmployeeReportDto createDto = new CreateEmployeeReportDto();
        EmployeeReportDto expectedDto = new EmployeeReportDto();

        when(employeeReportRestService.createEmployeeReport(createDto)).thenReturn(expectedDto);

        EmployeeReportDto result = employeeReportRestController.createEmployeeReport(createDto);

        assertSame(expectedDto, result);

        verify(employeeReportRestService).createEmployeeReport(createDto);
    }

    @Test
    void createEmployeeReport_shouldReturnNullWhenServiceReturnsNull() {
        CreateEmployeeReportDto createDto = new CreateEmployeeReportDto();

        when(employeeReportRestService.createEmployeeReport(createDto)).thenReturn(null);

        EmployeeReportDto result = employeeReportRestController.createEmployeeReport(createDto);

        assertNull(result);

        verify(employeeReportRestService).createEmployeeReport(createDto);
    }

    @Test
    void createEmployeeReport_shouldPropagateServiceException() {
        CreateEmployeeReportDto createDto = new CreateEmployeeReportDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.report.notFound");

        when(employeeReportRestService.createEmployeeReport(createDto)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeReportRestController.createEmployeeReport(createDto));

        assertSame(exception, thrown);

        verify(employeeReportRestService).createEmployeeReport(createDto);
    }

    @Test
    void getAllEmployeeReports_shouldReturnReports() {
        List<EmployeeReportDto> expectedReports = List.of(new EmployeeReportDto(), new EmployeeReportDto());

        when(employeeReportRestService.getAllEmployeeReports()).thenReturn(expectedReports);

        List<EmployeeReportDto> result = employeeReportRestController.getAllEmployeeReports();

        assertSame(expectedReports, result);

        verify(employeeReportRestService).getAllEmployeeReports();
    }

    @Test
    void getAllEmployeeReports_shouldReturnNullWhenServiceReturnsNull() {
        when(employeeReportRestService.getAllEmployeeReports()).thenReturn(null);

        List<EmployeeReportDto> result = employeeReportRestController.getAllEmployeeReports();

        assertNull(result);

        verify(employeeReportRestService).getAllEmployeeReports();
    }

    @Test
    void getAllEmployeeReports_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.report.notFound");

        when(employeeReportRestService.getAllEmployeeReports()).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeReportRestController.getAllEmployeeReports());

        assertSame(exception, thrown);

        verify(employeeReportRestService).getAllEmployeeReports();
    }

    @Test
    void getEmployeeReportById_shouldReturnReport() {
        EmployeeReportDto expectedDto = new EmployeeReportDto();

        when(employeeReportRestService.getEmployeeReportById(reportUuid)).thenReturn(expectedDto);

        EmployeeReportDto result = employeeReportRestController.getEmployeeReportById(reportUuid);

        assertSame(expectedDto, result);

        verify(employeeReportRestService).getEmployeeReportById(reportUuid);
    }

    @Test
    void getEmployeeReportById_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.report.notFound");

        when(employeeReportRestService.getEmployeeReportById(reportUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeReportRestController.getEmployeeReportById(reportUuid));

        assertSame(exception, thrown);

        verify(employeeReportRestService).getEmployeeReportById(reportUuid);
    }

    @Test
    void updateEmployeeReport_shouldReturnUpdatedReport() {
        UpdateEmployeeReportDto updateDto = new UpdateEmployeeReportDto();
        EmployeeReportDto expectedDto = new EmployeeReportDto();

        when(employeeReportRestService.updateEmployeeReport(reportUuid, updateDto))
                .thenReturn(expectedDto);

        EmployeeReportDto result = employeeReportRestController.updateEmployeeReport(reportUuid, updateDto);

        assertSame(expectedDto, result);

        verify(employeeReportRestService).updateEmployeeReport(reportUuid, updateDto);
    }

    @Test
    void updateEmployeeReport_shouldPropagateServiceException() {
        UpdateEmployeeReportDto updateDto = new UpdateEmployeeReportDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.report.notFound");

        when(employeeReportRestService.updateEmployeeReport(reportUuid, updateDto))
                .thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeReportRestController.updateEmployeeReport(reportUuid, updateDto));

        assertSame(exception, thrown);

        verify(employeeReportRestService).updateEmployeeReport(reportUuid, updateDto);
    }

    @Test
    void deleteEmployeeReport_shouldReturnDeleteDto() {
        DeleteDto expectedDto = new DeleteDto();

        when(employeeReportRestService.deleteEmployeeReport(reportUuid)).thenReturn(expectedDto);

        DeleteDto result = employeeReportRestController.deleteEmployeeReportById(reportUuid);

        assertSame(expectedDto, result);

        verify(employeeReportRestService).deleteEmployeeReport(reportUuid);
    }

    @Test
    void deleteEmployeeReport_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.report.notFound");

        when(employeeReportRestService.deleteEmployeeReport(reportUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeReportRestController.deleteEmployeeReportById(reportUuid));

        assertSame(exception, thrown);

        verify(employeeReportRestService).deleteEmployeeReport(reportUuid);
    }
}
