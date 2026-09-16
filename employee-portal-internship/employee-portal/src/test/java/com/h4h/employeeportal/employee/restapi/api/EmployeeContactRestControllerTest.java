package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeContactRestService;
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
class EmployeeContactRestControllerTest {

    @Mock
    private EmployeeContactRestService employeeContactRestService;

    @InjectMocks
    private EmployeeContactRestController employeeContactRestController;

    private UUID employeeContactUuid;

    @BeforeEach
    void setUp() {
        employeeContactUuid = UUID.randomUUID();
    }

    @Test
    void createEmployeeContact_shouldReturnCreatedContact() {
        CreateEmployeeContactDto createDto = new CreateEmployeeContactDto();

        EmployeeContactDto expectedDto = new EmployeeContactDto();

        when(employeeContactRestService.createEmployeeContact(createDto)).thenReturn(expectedDto);

        EmployeeContactDto result = employeeContactRestController.createEmployeeContact(createDto);

        assertSame(expectedDto, result);

        verify(employeeContactRestService).createEmployeeContact(createDto);
    }

    @Test
    void createEmployeeContact_shouldPropagateServiceException() {
        CreateEmployeeContactDto createDto = new CreateEmployeeContactDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.contact.notFound");

        when(employeeContactRestService.createEmployeeContact(createDto)).thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeContactRestController.createEmployeeContact(createDto)));

        verify(employeeContactRestService).createEmployeeContact(createDto);
    }

    @Test
    void getAllEmployeeContacts_shouldReturnAllContacts() {
        EmployeeContactDto firstDto = new EmployeeContactDto();

        EmployeeContactDto secondDto = new EmployeeContactDto();

        List<EmployeeContactDto> expectedDtos = List.of(firstDto, secondDto);

        when(employeeContactRestService.getAllEmployeeContacts()).thenReturn(expectedDtos);

        List<EmployeeContactDto> result = employeeContactRestController.getAllEmployeeContacts();

        assertSame(expectedDtos, result);

        verify(employeeContactRestService).getAllEmployeeContacts();
    }

    @Test
    void getAllEmployeeContacts_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.contact.notFound");

        when(employeeContactRestService.getAllEmployeeContacts()).thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class, () -> employeeContactRestController.getAllEmployeeContacts()));

        verify(employeeContactRestService).getAllEmployeeContacts();
    }

    @Test
    void getEmployeeContactById_shouldReturnContact() {
        EmployeeContactDto expectedDto = new EmployeeContactDto();

        when(employeeContactRestService.getEmployeeContactById(employeeContactUuid))
                .thenReturn(expectedDto);

        EmployeeContactDto result = employeeContactRestController.getEmployeeContactById(employeeContactUuid);

        assertSame(expectedDto, result);

        verify(employeeContactRestService).getEmployeeContactById(employeeContactUuid);
    }

    @Test
    void getEmployeeContactById_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.contact.notFound");

        when(employeeContactRestService.getEmployeeContactById(employeeContactUuid))
                .thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeContactRestController.getEmployeeContactById(employeeContactUuid)));

        verify(employeeContactRestService).getEmployeeContactById(employeeContactUuid);
    }

    @Test
    void updateEmployeeContact_shouldReturnUpdatedContact() {
        UpdateEmployeeContactDto updateDto = new UpdateEmployeeContactDto();

        EmployeeContactDto expectedDto = new EmployeeContactDto();

        when(employeeContactRestService.updateEmployeeContact(employeeContactUuid, updateDto))
                .thenReturn(expectedDto);

        EmployeeContactDto result = employeeContactRestController.updateEmployeeContact(employeeContactUuid, updateDto);

        assertSame(expectedDto, result);

        verify(employeeContactRestService).updateEmployeeContact(employeeContactUuid, updateDto);
    }

    @Test
    void updateEmployeeContact_shouldPropagateNotFoundException() {
        UpdateEmployeeContactDto updateDto = new UpdateEmployeeContactDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.contact.notFound");

        when(employeeContactRestService.updateEmployeeContact(employeeContactUuid, updateDto))
                .thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeContactRestController.updateEmployeeContact(employeeContactUuid, updateDto)));

        verify(employeeContactRestService).updateEmployeeContact(employeeContactUuid, updateDto);
    }

    @Test
    void deleteEmployeeContact_shouldReturnDeleteDto() {
        DeleteDto expectedDto = new DeleteDto();

        when(employeeContactRestService.deleteEmployeeContact(employeeContactUuid))
                .thenReturn(expectedDto);

        DeleteDto result = employeeContactRestController.deleteEmployeeContact(employeeContactUuid);

        assertSame(expectedDto, result);

        verify(employeeContactRestService).deleteEmployeeContact(employeeContactUuid);
    }

    @Test
    void deleteEmployeeContact_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.contact.notFound");

        when(employeeContactRestService.deleteEmployeeContact(employeeContactUuid))
                .thenThrow(exception);

        assertSame(
                exception,
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeContactRestController.deleteEmployeeContact(employeeContactUuid)));

        verify(employeeContactRestService).deleteEmployeeContact(employeeContactUuid);
    }
}
