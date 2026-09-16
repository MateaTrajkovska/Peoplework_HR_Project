package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.EmployeeContact;
import com.h4h.employeeportal.employee.core.service.EmployeeContactService;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeContactDto;
import com.h4h.employeeportal.employee.restapi.mapper.EmployeeContactMapper;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeContactRestServiceTest {

    @Mock
    private EmployeeContactService employeeContactService;

    @Mock
    private EmployeeContactMapper employeeContactMapper;

    @InjectMocks
    private EmployeeContactRestService employeeContactRestService;

    private UUID contactUuid;
    private UUID employeeUuid;

    private EmployeeContact employeeContact;
    private EmployeeContact mappedEmployeeContact;
    private EmployeeContactDto employeeContactDto;
    private CreateEmployeeContactDto createEmployeeContactDto;
    private UpdateEmployeeContactDto updateEmployeeContactDto;

    @BeforeEach
    void setUp() {
        contactUuid = UUID.randomUUID();
        employeeUuid = UUID.randomUUID();

        employeeContact = mock(EmployeeContact.class);
        mappedEmployeeContact = mock(EmployeeContact.class);
        employeeContactDto = mock(EmployeeContactDto.class);
        createEmployeeContactDto = mock(CreateEmployeeContactDto.class);
        updateEmployeeContactDto = mock(UpdateEmployeeContactDto.class);
    }

    private ResourceNotFoundException notFoundException() {
        return new ResourceNotFoundException("employee.contact.notFound");
    }

    @Test
    void createEmployeeContact_shouldMapSaveAndReturnDto() {
        when(employeeContactMapper.toEntityCreate(createEmployeeContactDto)).thenReturn(mappedEmployeeContact);

        when(employeeContactService.createEmployeeContact(mappedEmployeeContact))
                .thenReturn(employeeContact);

        when(employeeContactMapper.toDto(employeeContact)).thenReturn(employeeContactDto);

        EmployeeContactDto result = employeeContactRestService.createEmployeeContact(createEmployeeContactDto);

        assertSame(employeeContactDto, result);

        verify(employeeContactMapper).toEntityCreate(createEmployeeContactDto);

        verify(employeeContactService).createEmployeeContact(mappedEmployeeContact);

        verify(employeeContactMapper).toDto(employeeContact);
    }

    @Test
    void createEmployeeContact_shouldReturnNullWhenServiceReturnsNull() {
        when(employeeContactMapper.toEntityCreate(createEmployeeContactDto)).thenReturn(mappedEmployeeContact);

        when(employeeContactService.createEmployeeContact(mappedEmployeeContact))
                .thenReturn(null);

        when(employeeContactMapper.toDto(null)).thenReturn(null);

        EmployeeContactDto result = employeeContactRestService.createEmployeeContact(createEmployeeContactDto);

        assertSame(null, result);

        verify(employeeContactMapper).toEntityCreate(createEmployeeContactDto);

        verify(employeeContactService).createEmployeeContact(mappedEmployeeContact);

        verify(employeeContactMapper).toDto(null);
    }

    @Test
    void createEmployeeContact_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Mapping failed");

        when(employeeContactMapper.toEntityCreate(createEmployeeContactDto)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeContactRestService.createEmployeeContact(createEmployeeContactDto));

        assertSame(exception, thrown);

        verify(employeeContactMapper).toEntityCreate(createEmployeeContactDto);

        verifyNoInteractions(employeeContactService);
    }

    @Test
    void createEmployeeContact_shouldPropagateServiceException() {
        when(employeeContactMapper.toEntityCreate(createEmployeeContactDto)).thenReturn(mappedEmployeeContact);

        RuntimeException exception = new RuntimeException("Create failed");

        when(employeeContactService.createEmployeeContact(mappedEmployeeContact))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeContactRestService.createEmployeeContact(createEmployeeContactDto));

        assertSame(exception, thrown);

        verify(employeeContactMapper).toEntityCreate(createEmployeeContactDto);

        verify(employeeContactService).createEmployeeContact(mappedEmployeeContact);

        verify(employeeContactMapper, never()).toDto(any());
    }

    @Test
    void getAllEmployeeContacts_shouldReturnMappedList() {
        EmployeeContact secondContact = mock(EmployeeContact.class);

        List<EmployeeContact> contacts = List.of(employeeContact, secondContact);

        List<EmployeeContactDto> expectedDtos = List.of(employeeContactDto, mock(EmployeeContactDto.class));

        when(employeeContactService.getAllEmployeeContacts()).thenReturn(contacts);

        when(employeeContactMapper.toDtoList(contacts)).thenReturn(expectedDtos);

        List<EmployeeContactDto> result = employeeContactRestService.getAllEmployeeContacts();

        assertSame(expectedDtos, result);

        verify(employeeContactService).getAllEmployeeContacts();

        verify(employeeContactMapper).toDtoList(contacts);
    }

    @Test
    void getAllEmployeeContacts_shouldReturnEmptyList() {
        List<EmployeeContact> contacts = List.of();
        List<EmployeeContactDto> expectedDtos = List.of();

        when(employeeContactService.getAllEmployeeContacts()).thenReturn(contacts);

        when(employeeContactMapper.toDtoList(contacts)).thenReturn(expectedDtos);

        List<EmployeeContactDto> result = employeeContactRestService.getAllEmployeeContacts();

        assertTrue(result.isEmpty());

        verify(employeeContactService).getAllEmployeeContacts();

        verify(employeeContactMapper).toDtoList(contacts);
    }

    @Test
    void getAllEmployeeContacts_shouldPropagateServiceException() {
        RuntimeException exception = new RuntimeException("Database error");

        when(employeeContactService.getAllEmployeeContacts()).thenThrow(exception);

        RuntimeException thrown =
                assertThrows(RuntimeException.class, () -> employeeContactRestService.getAllEmployeeContacts());

        assertSame(exception, thrown);

        verify(employeeContactService).getAllEmployeeContacts();

        verifyNoInteractions(employeeContactMapper);
    }

    @Test
    void getEmployeeContactById_shouldReturnMappedDto() {
        when(employeeContactService.getEmployeeContactById(contactUuid)).thenReturn(employeeContact);

        when(employeeContactMapper.toDto(employeeContact)).thenReturn(employeeContactDto);

        EmployeeContactDto result = employeeContactRestService.getEmployeeContactById(contactUuid);

        assertSame(employeeContactDto, result);

        verify(employeeContactService).getEmployeeContactById(contactUuid);

        verify(employeeContactMapper).toDto(employeeContact);
    }

    @Test
    void getEmployeeContactById_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeContactService.getEmployeeContactById(contactUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeContactRestService.getEmployeeContactById(contactUuid));

        assertSame(exception, thrown);

        verify(employeeContactService).getEmployeeContactById(contactUuid);

        verifyNoInteractions(employeeContactMapper);
    }

    @Test
    void updateEmployeeContact_shouldMapUpdateAndReturnDto() {
        when(employeeContactMapper.toEntityUpdate(updateEmployeeContactDto)).thenReturn(mappedEmployeeContact);

        when(employeeContactService.updateEmployeeContact(contactUuid, mappedEmployeeContact))
                .thenReturn(employeeContact);

        when(employeeContactMapper.toDto(employeeContact)).thenReturn(employeeContactDto);

        EmployeeContactDto result =
                employeeContactRestService.updateEmployeeContact(contactUuid, updateEmployeeContactDto);

        assertSame(employeeContactDto, result);

        verify(employeeContactMapper).toEntityUpdate(updateEmployeeContactDto);

        verify(employeeContactService).updateEmployeeContact(contactUuid, mappedEmployeeContact);

        verify(employeeContactMapper).toDto(employeeContact);
    }

    @Test
    void updateEmployeeContact_shouldPropagateNotFoundException() {
        when(employeeContactMapper.toEntityUpdate(updateEmployeeContactDto)).thenReturn(mappedEmployeeContact);

        ResourceNotFoundException exception = notFoundException();

        when(employeeContactService.updateEmployeeContact(contactUuid, mappedEmployeeContact))
                .thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeContactRestService.updateEmployeeContact(contactUuid, updateEmployeeContactDto));

        assertSame(exception, thrown);

        verify(employeeContactMapper).toEntityUpdate(updateEmployeeContactDto);

        verify(employeeContactService).updateEmployeeContact(contactUuid, mappedEmployeeContact);

        verify(employeeContactMapper, never()).toDto(any());
    }

    @Test
    void updateEmployeeContact_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Mapping failed");

        when(employeeContactMapper.toEntityUpdate(updateEmployeeContactDto)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeContactRestService.updateEmployeeContact(contactUuid, updateEmployeeContactDto));

        assertSame(exception, thrown);

        verify(employeeContactMapper).toEntityUpdate(updateEmployeeContactDto);

        verifyNoInteractions(employeeContactService);
    }

    @Test
    void deleteEmployeeContact_shouldDeleteAndReturnDto() {
        DeleteDto expectedDto = new DeleteDto();
        expectedDto.setUuid(contactUuid);

        when(employeeContactService.deleteEmployeeContact(contactUuid)).thenReturn(contactUuid);

        when(employeeContactMapper.toDeletedDto(contactUuid)).thenReturn(expectedDto);

        DeleteDto result = employeeContactRestService.deleteEmployeeContact(contactUuid);

        assertSame(expectedDto, result);

        verify(employeeContactService).deleteEmployeeContact(contactUuid);

        verify(employeeContactMapper).toDeletedDto(contactUuid);
    }

    @Test
    void deleteEmployeeContact_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeContactService.deleteEmployeeContact(contactUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeContactRestService.deleteEmployeeContact(contactUuid));

        assertSame(exception, thrown);

        verify(employeeContactService).deleteEmployeeContact(contactUuid);

        verifyNoInteractions(employeeContactMapper);
    }

    @Test
    void deleteEmployeeContact_shouldPropagateMapperException() {
        when(employeeContactService.deleteEmployeeContact(contactUuid)).thenReturn(contactUuid);

        RuntimeException exception = new RuntimeException("Delete mapping failed");

        when(employeeContactMapper.toDeletedDto(contactUuid)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class, () -> employeeContactRestService.deleteEmployeeContact(contactUuid));

        assertSame(exception, thrown);

        verify(employeeContactService).deleteEmployeeContact(contactUuid);

        verify(employeeContactMapper).toDeletedDto(contactUuid);
    }
}
