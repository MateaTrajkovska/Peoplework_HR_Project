package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.EmployeeAddress;
import com.h4h.employeeportal.employee.core.service.EmployeeAddressService;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeAddressDto;
import com.h4h.employeeportal.employee.restapi.mapper.EmployeeAddressMapper;
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
class EmployeeAddressRestServiceTest {

    @Mock
    private EmployeeAddressService employeeAddressService;

    @Mock
    private EmployeeAddressMapper employeeAddressMapper;

    @InjectMocks
    private EmployeeAddressRestService employeeAddressRestService;

    private UUID addressUuid;
    private EmployeeAddress employeeAddress;
    private EmployeeAddress mappedEmployeeAddress;
    private EmployeeAddressDto employeeAddressDto;
    private CreateEmployeeAddressDto createEmployeeAddressDto;
    private UpdateEmployeeAddressDto updateEmployeeAddressDto;

    @BeforeEach
    void setUp() {
        addressUuid = UUID.randomUUID();

        employeeAddress = mock(EmployeeAddress.class);
        mappedEmployeeAddress = mock(EmployeeAddress.class);
        employeeAddressDto = mock(EmployeeAddressDto.class);
        createEmployeeAddressDto = mock(CreateEmployeeAddressDto.class);
        updateEmployeeAddressDto = mock(UpdateEmployeeAddressDto.class);
    }

    private ResourceNotFoundException notFoundException() {
        return new ResourceNotFoundException("employee.address.notFound");
    }

    @Test
    void createEmployeeAddress_shouldMapSaveAndReturnDto() {
        when(employeeAddressMapper.toEntityCreate(createEmployeeAddressDto)).thenReturn(mappedEmployeeAddress);

        when(employeeAddressService.createEmployeeAddress(mappedEmployeeAddress))
                .thenReturn(employeeAddress);

        when(employeeAddressMapper.toDto(employeeAddress)).thenReturn(employeeAddressDto);

        EmployeeAddressDto result = employeeAddressRestService.createEmployeeAddress(createEmployeeAddressDto);

        assertSame(employeeAddressDto, result);

        verify(employeeAddressMapper).toEntityCreate(createEmployeeAddressDto);

        verify(employeeAddressService).createEmployeeAddress(mappedEmployeeAddress);

        verify(employeeAddressMapper).toDto(employeeAddress);
    }

    @Test
    void createEmployeeAddress_shouldReturnNullWhenServiceReturnsNull() {
        when(employeeAddressMapper.toEntityCreate(createEmployeeAddressDto)).thenReturn(mappedEmployeeAddress);

        when(employeeAddressService.createEmployeeAddress(mappedEmployeeAddress))
                .thenReturn(null);

        when(employeeAddressMapper.toDto(null)).thenReturn(null);

        EmployeeAddressDto result = employeeAddressRestService.createEmployeeAddress(createEmployeeAddressDto);

        assertSame(null, result);

        verify(employeeAddressMapper).toEntityCreate(createEmployeeAddressDto);

        verify(employeeAddressService).createEmployeeAddress(mappedEmployeeAddress);

        verify(employeeAddressMapper).toDto(null);
    }

    @Test
    void createEmployeeAddress_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Mapping failed");

        when(employeeAddressMapper.toEntityCreate(createEmployeeAddressDto)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeAddressRestService.createEmployeeAddress(createEmployeeAddressDto));

        assertSame(exception, thrown);

        verify(employeeAddressMapper).toEntityCreate(createEmployeeAddressDto);

        verifyNoInteractions(employeeAddressService);
    }

    @Test
    void createEmployeeAddress_shouldPropagateServiceException() {
        when(employeeAddressMapper.toEntityCreate(createEmployeeAddressDto)).thenReturn(mappedEmployeeAddress);

        RuntimeException exception = new RuntimeException("Create failed");

        when(employeeAddressService.createEmployeeAddress(mappedEmployeeAddress))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeAddressRestService.createEmployeeAddress(createEmployeeAddressDto));

        assertSame(exception, thrown);

        verify(employeeAddressMapper).toEntityCreate(createEmployeeAddressDto);

        verify(employeeAddressService).createEmployeeAddress(mappedEmployeeAddress);

        verify(employeeAddressMapper, never()).toDto(any());
    }

    @Test
    void getAllEmployeeAddresses_shouldReturnMappedList() {
        EmployeeAddress secondAddress = mock(EmployeeAddress.class);

        List<EmployeeAddress> addresses = List.of(employeeAddress, secondAddress);

        List<EmployeeAddressDto> expectedDtos = List.of(employeeAddressDto, mock(EmployeeAddressDto.class));

        when(employeeAddressService.getAllEmployeeAddresses()).thenReturn(addresses);

        when(employeeAddressMapper.toDtoList(addresses)).thenReturn(expectedDtos);

        List<EmployeeAddressDto> result = employeeAddressRestService.getAllEmployeeAddresses();

        assertSame(expectedDtos, result);

        verify(employeeAddressService).getAllEmployeeAddresses();

        verify(employeeAddressMapper).toDtoList(addresses);
    }

    @Test
    void getAllEmployeeAddresses_shouldReturnEmptyList() {
        List<EmployeeAddress> addresses = List.of();
        List<EmployeeAddressDto> expectedDtos = List.of();

        when(employeeAddressService.getAllEmployeeAddresses()).thenReturn(addresses);

        when(employeeAddressMapper.toDtoList(addresses)).thenReturn(expectedDtos);

        List<EmployeeAddressDto> result = employeeAddressRestService.getAllEmployeeAddresses();

        assertTrue(result.isEmpty());

        verify(employeeAddressService).getAllEmployeeAddresses();

        verify(employeeAddressMapper).toDtoList(addresses);
    }

    @Test
    void getAllEmployeeAddresses_shouldPropagateServiceException() {
        RuntimeException exception = new RuntimeException("Database error");

        when(employeeAddressService.getAllEmployeeAddresses()).thenThrow(exception);

        RuntimeException thrown =
                assertThrows(RuntimeException.class, () -> employeeAddressRestService.getAllEmployeeAddresses());

        assertSame(exception, thrown);

        verify(employeeAddressService).getAllEmployeeAddresses();

        verifyNoInteractions(employeeAddressMapper);
    }

    @Test
    void getEmployeeAddressById_shouldReturnMappedDto() {
        when(employeeAddressService.getEmployeeAddressById(addressUuid)).thenReturn(employeeAddress);

        when(employeeAddressMapper.toDto(employeeAddress)).thenReturn(employeeAddressDto);

        EmployeeAddressDto result = employeeAddressRestService.getEmployeeAddressById(addressUuid);

        assertSame(employeeAddressDto, result);

        verify(employeeAddressService).getEmployeeAddressById(addressUuid);

        verify(employeeAddressMapper).toDto(employeeAddress);
    }

    @Test
    void getEmployeeAddressById_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeAddressService.getEmployeeAddressById(addressUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeAddressRestService.getEmployeeAddressById(addressUuid));

        assertSame(exception, thrown);

        verify(employeeAddressService).getEmployeeAddressById(addressUuid);

        verifyNoInteractions(employeeAddressMapper);
    }

    @Test
    void updateEmployeeAddress_shouldMapUpdateAndReturnDto() {
        when(employeeAddressMapper.toEntityUpdate(updateEmployeeAddressDto)).thenReturn(mappedEmployeeAddress);

        when(employeeAddressService.updateEmployeeAddress(addressUuid, mappedEmployeeAddress))
                .thenReturn(employeeAddress);

        when(employeeAddressMapper.toDto(employeeAddress)).thenReturn(employeeAddressDto);

        EmployeeAddressDto result =
                employeeAddressRestService.updateEmployeeAddress(addressUuid, updateEmployeeAddressDto);

        assertSame(employeeAddressDto, result);

        verify(employeeAddressMapper).toEntityUpdate(updateEmployeeAddressDto);

        verify(employeeAddressService).updateEmployeeAddress(addressUuid, mappedEmployeeAddress);

        verify(employeeAddressMapper).toDto(employeeAddress);
    }

    @Test
    void updateEmployeeAddress_shouldPropagateNotFoundException() {
        when(employeeAddressMapper.toEntityUpdate(updateEmployeeAddressDto)).thenReturn(mappedEmployeeAddress);

        ResourceNotFoundException exception = notFoundException();

        when(employeeAddressService.updateEmployeeAddress(addressUuid, mappedEmployeeAddress))
                .thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeAddressRestService.updateEmployeeAddress(addressUuid, updateEmployeeAddressDto));

        assertSame(exception, thrown);

        verify(employeeAddressMapper).toEntityUpdate(updateEmployeeAddressDto);

        verify(employeeAddressService).updateEmployeeAddress(addressUuid, mappedEmployeeAddress);

        verify(employeeAddressMapper, never()).toDto(any());
    }

    @Test
    void updateEmployeeAddress_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Mapping failed");

        when(employeeAddressMapper.toEntityUpdate(updateEmployeeAddressDto)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeAddressRestService.updateEmployeeAddress(addressUuid, updateEmployeeAddressDto));

        assertSame(exception, thrown);

        verify(employeeAddressMapper).toEntityUpdate(updateEmployeeAddressDto);

        verifyNoInteractions(employeeAddressService);
    }

    @Test
    void deleteEmployeeAddress_shouldDeleteAndReturnDto() {
        when(employeeAddressService.deleteEmployeeAddress(addressUuid)).thenReturn(addressUuid);

        DeleteDto expectedDto = new DeleteDto();
        expectedDto.setUuid(addressUuid);

        when(employeeAddressMapper.toDeletedDto(addressUuid)).thenReturn(expectedDto);

        DeleteDto result = employeeAddressRestService.deleteEmployeeAddress(addressUuid);

        assertSame(expectedDto, result);

        verify(employeeAddressService).deleteEmployeeAddress(addressUuid);

        verify(employeeAddressMapper).toDeletedDto(addressUuid);
    }

    @Test
    void deleteEmployeeAddress_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeAddressService.deleteEmployeeAddress(addressUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeAddressRestService.deleteEmployeeAddress(addressUuid));

        assertSame(exception, thrown);

        verify(employeeAddressService).deleteEmployeeAddress(addressUuid);

        verifyNoInteractions(employeeAddressMapper);
    }

    @Test
    void deleteEmployeeAddress_shouldPropagateMapperException() {
        when(employeeAddressService.deleteEmployeeAddress(addressUuid)).thenReturn(addressUuid);

        RuntimeException exception = new RuntimeException("Delete mapping failed");

        when(employeeAddressMapper.toDeletedDto(addressUuid)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class, () -> employeeAddressRestService.deleteEmployeeAddress(addressUuid));

        assertSame(exception, thrown);

        verify(employeeAddressService).deleteEmployeeAddress(addressUuid);

        verify(employeeAddressMapper).toDeletedDto(addressUuid);
    }
}
