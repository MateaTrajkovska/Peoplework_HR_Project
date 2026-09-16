package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeFinanceRestService;
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
class EmployeeFinanceRestControllerTest {

    @Mock
    private EmployeeFinanceRestService employeeFinanceRestService;

    @InjectMocks
    private EmployeeFinanceRestController employeeFinanceRestController;

    private UUID employeeFinanceUuid;

    @BeforeEach
    void setUp() {
        employeeFinanceUuid = UUID.randomUUID();
    }

    @Test
    void createEmployeeFinance_shouldReturnCreatedFinance() {
        CreateEmployeeFinanceDto createDto = new CreateEmployeeFinanceDto();
        EmployeeFinanceDto expectedDto = new EmployeeFinanceDto();

        when(employeeFinanceRestService.createEmployeeFinance(createDto)).thenReturn(expectedDto);

        EmployeeFinanceDto result = employeeFinanceRestController.createEmployeeFinance(createDto);

        assertSame(expectedDto, result);

        verify(employeeFinanceRestService).createEmployeeFinance(createDto);
    }

    @Test
    void createEmployeeFinance_shouldPropagateServiceException() {
        CreateEmployeeFinanceDto createDto = new CreateEmployeeFinanceDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.finance.notFound");

        when(employeeFinanceRestService.createEmployeeFinance(createDto)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeFinanceRestController.createEmployeeFinance(createDto));

        assertSame(exception, thrown);

        verify(employeeFinanceRestService).createEmployeeFinance(createDto);
    }

    @Test
    void getAllEmployeeFinances_shouldReturnAllFinances() {
        EmployeeFinanceDto firstDto = new EmployeeFinanceDto();
        EmployeeFinanceDto secondDto = new EmployeeFinanceDto();

        List<EmployeeFinanceDto> expected = List.of(firstDto, secondDto);

        when(employeeFinanceRestService.getAllEmployeeFinances()).thenReturn(expected);

        List<EmployeeFinanceDto> result = employeeFinanceRestController.getAllEmployeeFinances();

        assertAll(() -> assertSame(expected, result), () -> assertEquals(2, result.size()));

        verify(employeeFinanceRestService).getAllEmployeeFinances();
    }

    @Test
    void getAllEmployeeFinances_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.finance.notFound");

        when(employeeFinanceRestService.getAllEmployeeFinances()).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeFinanceRestController.getAllEmployeeFinances());

        assertSame(exception, thrown);

        verify(employeeFinanceRestService).getAllEmployeeFinances();
    }

    @Test
    void getEmployeeFinanceById_shouldReturnFinance() {
        EmployeeFinanceDto expectedDto = new EmployeeFinanceDto();

        when(employeeFinanceRestService.getEmployeeFinanceById(employeeFinanceUuid))
                .thenReturn(expectedDto);

        EmployeeFinanceDto result = employeeFinanceRestController.getEmployeeFinanceById(employeeFinanceUuid);

        assertSame(expectedDto, result);

        verify(employeeFinanceRestService).getEmployeeFinanceById(employeeFinanceUuid);
    }

    @Test
    void getEmployeeFinanceById_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.finance.notFound");

        when(employeeFinanceRestService.getEmployeeFinanceById(employeeFinanceUuid))
                .thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeFinanceRestController.getEmployeeFinanceById(employeeFinanceUuid));

        assertSame(exception, thrown);

        verify(employeeFinanceRestService).getEmployeeFinanceById(employeeFinanceUuid);
    }

    @Test
    void updateEmployeeFinance_shouldReturnUpdatedFinance() {
        UpdateEmployeeFinanceDto updateDto = new UpdateEmployeeFinanceDto();
        EmployeeFinanceDto expectedDto = new EmployeeFinanceDto();

        when(employeeFinanceRestService.updateEmployeeFinance(employeeFinanceUuid, updateDto))
                .thenReturn(expectedDto);

        EmployeeFinanceDto result = employeeFinanceRestController.updateEmployeeFinance(employeeFinanceUuid, updateDto);

        assertSame(expectedDto, result);

        verify(employeeFinanceRestService).updateEmployeeFinance(employeeFinanceUuid, updateDto);
    }

    @Test
    void updateEmployeeFinance_shouldPropagateNotFoundException() {
        UpdateEmployeeFinanceDto updateDto = new UpdateEmployeeFinanceDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.finance.notFound");

        when(employeeFinanceRestService.updateEmployeeFinance(employeeFinanceUuid, updateDto))
                .thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeFinanceRestController.updateEmployeeFinance(employeeFinanceUuid, updateDto));

        assertSame(exception, thrown);

        verify(employeeFinanceRestService).updateEmployeeFinance(employeeFinanceUuid, updateDto);
    }

    @Test
    void deleteEmployeeFinance_shouldReturnDeleteDto() {
        DeleteDto expectedDto = new DeleteDto();

        when(employeeFinanceRestService.deleteEmployeeFinance(employeeFinanceUuid))
                .thenReturn(expectedDto);

        DeleteDto result = employeeFinanceRestController.deleteEmployeeFinance(employeeFinanceUuid);

        assertSame(expectedDto, result);

        verify(employeeFinanceRestService).deleteEmployeeFinance(employeeFinanceUuid);
    }

    @Test
    void deleteEmployeeFinance_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.finance.notFound");

        when(employeeFinanceRestService.deleteEmployeeFinance(employeeFinanceUuid))
                .thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeFinanceRestController.deleteEmployeeFinance(employeeFinanceUuid));

        assertSame(exception, thrown);

        verify(employeeFinanceRestService).deleteEmployeeFinance(employeeFinanceUuid);
    }
}
