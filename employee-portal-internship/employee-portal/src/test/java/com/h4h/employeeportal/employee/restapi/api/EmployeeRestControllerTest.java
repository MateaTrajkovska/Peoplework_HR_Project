package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeDetailsDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeListDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeRestService;
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
class EmployeeRestControllerTest {

    @Mock
    private EmployeeRestService employeeRestService;

    @InjectMocks
    private EmployeeRestController employeeRestController;

    private UUID employeeUuid;

    @BeforeEach
    void setUp() {
        employeeUuid = UUID.randomUUID();
    }

    @Test
    void createEmployee_shouldReturnCreatedEmployee() {
        CreateEmployeeDto createDto = new CreateEmployeeDto();
        EmployeeDto expectedDto = new EmployeeDto();

        when(employeeRestService.createEmployee(createDto)).thenReturn(expectedDto);

        EmployeeDto result = employeeRestController.createEmployee(createDto);

        assertSame(expectedDto, result);

        verify(employeeRestService).createEmployee(createDto);
    }

    @Test
    void createEmployee_shouldPropagateServiceException() {
        CreateEmployeeDto createDto = new CreateEmployeeDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.notFound");

        when(employeeRestService.createEmployee(createDto)).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> employeeRestController.createEmployee(createDto));

        assertSame(exception, thrown);

        verify(employeeRestService).createEmployee(createDto);
    }

    @Test
    void getAllEmployees_shouldReturnEmployees() {
        List<EmployeeListDto> expectedEmployees = List.of(new EmployeeListDto(), new EmployeeListDto());

        when(employeeRestService.getAllEmployees()).thenReturn(expectedEmployees);

        List<EmployeeListDto> result = employeeRestController.getAllEmployees();

        assertSame(expectedEmployees, result);

        verify(employeeRestService).getAllEmployees();
    }

    @Test
    void getAllEmployees_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.notFound");

        when(employeeRestService.getAllEmployees()).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> employeeRestController.getAllEmployees());

        assertSame(exception, thrown);

        verify(employeeRestService).getAllEmployees();
    }

    @Test
    void getEmployeeById_shouldReturnEmployee() {
        EmployeeDetailsDto expectedDto = new EmployeeDetailsDto();

        when(employeeRestService.getEmployeeById(employeeUuid)).thenReturn(expectedDto);

        EmployeeDetailsDto result = employeeRestController.getEmployeeById(employeeUuid);

        assertSame(expectedDto, result);

        verify(employeeRestService).getEmployeeById(employeeUuid);
    }

    @Test
    void getEmployeeById_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.notFound");

        when(employeeRestService.getEmployeeById(employeeUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeRestController.getEmployeeById(employeeUuid));

        assertSame(exception, thrown);

        verify(employeeRestService).getEmployeeById(employeeUuid);
    }

    @Test
    void updateEmployee_shouldReturnUpdatedEmployee() {
        UpdateEmployeeDto updateDto = new UpdateEmployeeDto();
        EmployeeDto expectedDto = new EmployeeDto();

        when(employeeRestService.updateEmployee(employeeUuid, updateDto)).thenReturn(expectedDto);

        EmployeeDto result = employeeRestController.updateEmployee(employeeUuid, updateDto);

        assertSame(expectedDto, result);

        verify(employeeRestService).updateEmployee(employeeUuid, updateDto);
    }

    @Test
    void updateEmployee_shouldPropagateServiceException() {
        UpdateEmployeeDto updateDto = new UpdateEmployeeDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.notFound");

        when(employeeRestService.updateEmployee(employeeUuid, updateDto)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeRestController.updateEmployee(employeeUuid, updateDto));

        assertSame(exception, thrown);

        verify(employeeRestService).updateEmployee(employeeUuid, updateDto);
    }

    @Test
    void deleteEmployee_shouldReturnDeleteDto() {
        DeleteDto expectedDto = new DeleteDto();

        when(employeeRestService.deleteEmployee(employeeUuid)).thenReturn(expectedDto);

        DeleteDto result = employeeRestController.deleteEmployee(employeeUuid);

        assertSame(expectedDto, result);

        verify(employeeRestService).deleteEmployee(employeeUuid);
    }

    @Test
    void deleteEmployee_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.notFound");

        when(employeeRestService.deleteEmployee(employeeUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeRestController.deleteEmployee(employeeUuid));

        assertSame(exception, thrown);

        verify(employeeRestService).deleteEmployee(employeeUuid);
    }
}
