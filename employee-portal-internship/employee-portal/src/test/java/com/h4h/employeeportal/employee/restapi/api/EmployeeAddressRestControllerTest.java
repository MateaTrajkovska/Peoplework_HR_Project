package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeAddressRestService;
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
class EmployeeAddressRestControllerTest {

    @Mock
    private EmployeeAddressRestService employeeAddressRestService;

    @InjectMocks
    private EmployeeAddressRestController employeeAddressRestController;

    private UUID employeeAddressUuid;

    @BeforeEach
    void setUp() {
        employeeAddressUuid = UUID.randomUUID();
    }

    @Test
    void createEmployeeAddress_shouldReturnCreatedAddress() {
        CreateEmployeeAddressDto createDto = new CreateEmployeeAddressDto();

        EmployeeAddressDto expectedDto = new EmployeeAddressDto();

        when(employeeAddressRestService.createEmployeeAddress(createDto)).thenReturn(expectedDto);

        EmployeeAddressDto result = employeeAddressRestController.createEmployeeAddress(createDto);

        assertSame(expectedDto, result);

        verify(employeeAddressRestService).createEmployeeAddress(createDto);
    }

    @Test
    void createEmployeeAddress_shouldPropagateServiceException() {
        CreateEmployeeAddressDto createDto = new CreateEmployeeAddressDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.address.notFound");

        when(employeeAddressRestService.createEmployeeAddress(createDto)).thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeAddressRestController.createEmployeeAddress(createDto)));

        verify(employeeAddressRestService).createEmployeeAddress(createDto);
    }

    @Test
    void getAllEmployeeAddresses_shouldReturnAllAddresses() {
        EmployeeAddressDto firstDto = new EmployeeAddressDto();
        EmployeeAddressDto secondDto = new EmployeeAddressDto();

        List<EmployeeAddressDto> expectedDtos = List.of(firstDto, secondDto);

        when(employeeAddressRestService.getAllEmployeeAddresses()).thenReturn(expectedDtos);

        List<EmployeeAddressDto> result = employeeAddressRestController.getAllEmployeeAddresses();

        assertSame(expectedDtos, result);

        verify(employeeAddressRestService).getAllEmployeeAddresses();
    }

    @Test
    void getAllEmployeeAddresses_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.address.notFound");

        when(employeeAddressRestService.getAllEmployeeAddresses()).thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeAddressRestController.getAllEmployeeAddresses()));

        verify(employeeAddressRestService).getAllEmployeeAddresses();
    }

    @Test
    void getEmployeeAddressById_shouldReturnAddress() {
        EmployeeAddressDto expectedDto = new EmployeeAddressDto();

        when(employeeAddressRestService.getEmployeeAddressById(employeeAddressUuid))
                .thenReturn(expectedDto);

        EmployeeAddressDto result = employeeAddressRestController.getEmployeeAddressById(employeeAddressUuid);

        assertSame(expectedDto, result);

        verify(employeeAddressRestService).getEmployeeAddressById(employeeAddressUuid);
    }

    @Test
    void getEmployeeAddressById_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.address.notFound");

        when(employeeAddressRestService.getEmployeeAddressById(employeeAddressUuid))
                .thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeAddressRestController.getEmployeeAddressById(employeeAddressUuid)));

        verify(employeeAddressRestService).getEmployeeAddressById(employeeAddressUuid);
    }

    @Test
    void updateEmployeeAddress_shouldReturnUpdatedAddress() {
        UpdateEmployeeAddressDto updateDto = new UpdateEmployeeAddressDto();

        EmployeeAddressDto expectedDto = new EmployeeAddressDto();

        when(employeeAddressRestService.updateEmployeeAddress(employeeAddressUuid, updateDto))
                .thenReturn(expectedDto);

        EmployeeAddressDto result = employeeAddressRestController.updateEmployeeAddress(employeeAddressUuid, updateDto);

        assertSame(expectedDto, result);

        verify(employeeAddressRestService).updateEmployeeAddress(employeeAddressUuid, updateDto);
    }

    @Test
    void updateEmployeeAddress_shouldPropagateNotFoundException() {
        UpdateEmployeeAddressDto updateDto = new UpdateEmployeeAddressDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.address.notFound");

        when(employeeAddressRestService.updateEmployeeAddress(employeeAddressUuid, updateDto))
                .thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeAddressRestController.updateEmployeeAddress(employeeAddressUuid, updateDto)));

        verify(employeeAddressRestService).updateEmployeeAddress(employeeAddressUuid, updateDto);
    }

    @Test
    void deleteEmployeeAddress_shouldReturnDeleteDto() {
        DeleteDto expectedDto = new DeleteDto();

        when(employeeAddressRestService.deleteEmployeeAddress(employeeAddressUuid))
                .thenReturn(expectedDto);

        DeleteDto result = employeeAddressRestController.deleteEmployeeAddress(employeeAddressUuid);

        assertSame(expectedDto, result);

        verify(employeeAddressRestService).deleteEmployeeAddress(employeeAddressUuid);
    }

    @Test
    void deleteEmployeeAddress_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.address.notFound");

        when(employeeAddressRestService.deleteEmployeeAddress(employeeAddressUuid))
                .thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeAddressRestController.deleteEmployeeAddress(employeeAddressUuid)));

        verify(employeeAddressRestService).deleteEmployeeAddress(employeeAddressUuid);
    }
}
