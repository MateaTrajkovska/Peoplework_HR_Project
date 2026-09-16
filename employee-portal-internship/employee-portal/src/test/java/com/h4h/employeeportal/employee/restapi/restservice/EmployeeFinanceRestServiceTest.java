package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.EmployeeFinance;
import com.h4h.employeeportal.employee.core.service.EmployeeFinanceService;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeFinanceDto;
import com.h4h.employeeportal.employee.restapi.mapper.EmployeeFinanceMapper;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeFinanceRestServiceTest {

    @Mock
    private EmployeeFinanceService employeeFinanceService;

    @Mock
    private EmployeeFinanceMapper employeeFinanceMapper;

    @InjectMocks
    private EmployeeFinanceRestService employeeFinanceRestService;

    private UUID financeUuid;

    private EmployeeFinance employeeFinance;
    private EmployeeFinance mappedEmployeeFinance;
    private EmployeeFinanceDto employeeFinanceDto;
    private CreateEmployeeFinanceDto createEmployeeFinanceDto;
    private UpdateEmployeeFinanceDto updateEmployeeFinanceDto;

    @BeforeEach
    void setUp() {
        financeUuid = UUID.randomUUID();

        employeeFinance = mock(EmployeeFinance.class);
        mappedEmployeeFinance = mock(EmployeeFinance.class);
        employeeFinanceDto = mock(EmployeeFinanceDto.class);
        createEmployeeFinanceDto = mock(CreateEmployeeFinanceDto.class);
        updateEmployeeFinanceDto = mock(UpdateEmployeeFinanceDto.class);
    }

    private ResourceNotFoundException notFoundException() {
        return new ResourceNotFoundException("employee.finance.notFound");
    }

    @Test
    void createEmployeeFinance_shouldMapSaveAndReturnDto() {
        when(employeeFinanceMapper.toEntityCreate(createEmployeeFinanceDto)).thenReturn(mappedEmployeeFinance);

        when(employeeFinanceService.createEmployeeFinance(mappedEmployeeFinance))
                .thenReturn(employeeFinance);

        when(employeeFinanceMapper.toDto(employeeFinance)).thenReturn(employeeFinanceDto);

        EmployeeFinanceDto result = employeeFinanceRestService.createEmployeeFinance(createEmployeeFinanceDto);

        assertSame(employeeFinanceDto, result);

        verify(employeeFinanceMapper).toEntityCreate(createEmployeeFinanceDto);

        verify(employeeFinanceService).createEmployeeFinance(mappedEmployeeFinance);

        verify(employeeFinanceMapper).toDto(employeeFinance);
    }

    @Test
    void createEmployeeFinance_shouldReturnNullWhenServiceReturnsNull() {
        when(employeeFinanceMapper.toEntityCreate(createEmployeeFinanceDto)).thenReturn(mappedEmployeeFinance);

        when(employeeFinanceService.createEmployeeFinance(mappedEmployeeFinance))
                .thenReturn(null);

        when(employeeFinanceMapper.toDto(null)).thenReturn(null);

        EmployeeFinanceDto result = employeeFinanceRestService.createEmployeeFinance(createEmployeeFinanceDto);

        assertNull(result);

        verify(employeeFinanceMapper).toEntityCreate(createEmployeeFinanceDto);

        verify(employeeFinanceService).createEmployeeFinance(mappedEmployeeFinance);

        verify(employeeFinanceMapper).toDto(null);
    }

    @Test
    void createEmployeeFinance_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Mapping failed");

        when(employeeFinanceMapper.toEntityCreate(createEmployeeFinanceDto)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeFinanceRestService.createEmployeeFinance(createEmployeeFinanceDto));

        assertSame(exception, thrown);

        verify(employeeFinanceMapper).toEntityCreate(createEmployeeFinanceDto);

        verifyNoInteractions(employeeFinanceService);
    }

    @Test
    void createEmployeeFinance_shouldPropagateServiceException() {
        when(employeeFinanceMapper.toEntityCreate(createEmployeeFinanceDto)).thenReturn(mappedEmployeeFinance);

        RuntimeException exception = new RuntimeException("Create failed");

        when(employeeFinanceService.createEmployeeFinance(mappedEmployeeFinance))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeFinanceRestService.createEmployeeFinance(createEmployeeFinanceDto));

        assertSame(exception, thrown);

        verify(employeeFinanceMapper).toEntityCreate(createEmployeeFinanceDto);

        verify(employeeFinanceService).createEmployeeFinance(mappedEmployeeFinance);

        verify(employeeFinanceMapper, never()).toDto(any());
    }

    @Test
    void getAllEmployeeFinances_shouldReturnMappedList() {
        EmployeeFinance secondFinance = mock(EmployeeFinance.class);

        List<EmployeeFinance> finances = List.of(employeeFinance, secondFinance);

        List<EmployeeFinanceDto> expectedDtos = List.of(employeeFinanceDto, mock(EmployeeFinanceDto.class));

        when(employeeFinanceService.getAllEmployeeFinances()).thenReturn(finances);

        when(employeeFinanceMapper.toDtoList(finances)).thenReturn(expectedDtos);

        List<EmployeeFinanceDto> result = employeeFinanceRestService.getAllEmployeeFinances();

        assertSame(expectedDtos, result);

        verify(employeeFinanceService).getAllEmployeeFinances();

        verify(employeeFinanceMapper).toDtoList(finances);
    }

    @Test
    void getAllEmployeeFinances_shouldReturnEmptyList() {
        List<EmployeeFinance> finances = List.of();
        List<EmployeeFinanceDto> expectedDtos = List.of();

        when(employeeFinanceService.getAllEmployeeFinances()).thenReturn(finances);

        when(employeeFinanceMapper.toDtoList(finances)).thenReturn(expectedDtos);

        List<EmployeeFinanceDto> result = employeeFinanceRestService.getAllEmployeeFinances();

        assertTrue(result.isEmpty());

        verify(employeeFinanceService).getAllEmployeeFinances();

        verify(employeeFinanceMapper).toDtoList(finances);
    }

    @Test
    void getAllEmployeeFinances_shouldPropagateServiceException() {
        RuntimeException exception = new RuntimeException("Database error");

        when(employeeFinanceService.getAllEmployeeFinances()).thenThrow(exception);

        RuntimeException thrown =
                assertThrows(RuntimeException.class, () -> employeeFinanceRestService.getAllEmployeeFinances());

        assertSame(exception, thrown);

        verify(employeeFinanceService).getAllEmployeeFinances();

        verifyNoInteractions(employeeFinanceMapper);
    }

    @Test
    void getEmployeeFinanceById_shouldReturnMappedDto() {
        when(employeeFinanceService.getEmployeeFinanceById(financeUuid)).thenReturn(employeeFinance);

        when(employeeFinanceMapper.toDto(employeeFinance)).thenReturn(employeeFinanceDto);

        EmployeeFinanceDto result = employeeFinanceRestService.getEmployeeFinanceById(financeUuid);

        assertSame(employeeFinanceDto, result);

        verify(employeeFinanceService).getEmployeeFinanceById(financeUuid);

        verify(employeeFinanceMapper).toDto(employeeFinance);
    }

    @Test
    void getEmployeeFinanceById_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeFinanceService.getEmployeeFinanceById(financeUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeFinanceRestService.getEmployeeFinanceById(financeUuid));

        assertSame(exception, thrown);

        verify(employeeFinanceService).getEmployeeFinanceById(financeUuid);

        verifyNoInteractions(employeeFinanceMapper);
    }

    @Test
    void updateEmployeeFinance_shouldMapUpdateAndReturnDto() {
        when(employeeFinanceMapper.toEntityUpdate(updateEmployeeFinanceDto)).thenReturn(mappedEmployeeFinance);

        when(employeeFinanceService.updateEmployeeFinance(financeUuid, mappedEmployeeFinance))
                .thenReturn(employeeFinance);

        when(employeeFinanceMapper.toDto(employeeFinance)).thenReturn(employeeFinanceDto);

        EmployeeFinanceDto result =
                employeeFinanceRestService.updateEmployeeFinance(financeUuid, updateEmployeeFinanceDto);

        assertSame(employeeFinanceDto, result);

        verify(employeeFinanceMapper).toEntityUpdate(updateEmployeeFinanceDto);

        verify(employeeFinanceService).updateEmployeeFinance(financeUuid, mappedEmployeeFinance);

        verify(employeeFinanceMapper).toDto(employeeFinance);
    }

    @Test
    void updateEmployeeFinance_shouldPropagateNotFoundException() {
        when(employeeFinanceMapper.toEntityUpdate(updateEmployeeFinanceDto)).thenReturn(mappedEmployeeFinance);

        ResourceNotFoundException exception = notFoundException();

        when(employeeFinanceService.updateEmployeeFinance(financeUuid, mappedEmployeeFinance))
                .thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeFinanceRestService.updateEmployeeFinance(financeUuid, updateEmployeeFinanceDto));

        assertSame(exception, thrown);

        verify(employeeFinanceMapper).toEntityUpdate(updateEmployeeFinanceDto);

        verify(employeeFinanceService).updateEmployeeFinance(financeUuid, mappedEmployeeFinance);

        verify(employeeFinanceMapper, never()).toDto(any());
    }

    @Test
    void updateEmployeeFinance_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Mapping failed");

        when(employeeFinanceMapper.toEntityUpdate(updateEmployeeFinanceDto)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeFinanceRestService.updateEmployeeFinance(financeUuid, updateEmployeeFinanceDto));

        assertSame(exception, thrown);

        verify(employeeFinanceMapper).toEntityUpdate(updateEmployeeFinanceDto);

        verifyNoInteractions(employeeFinanceService);
    }

    @Test
    void deleteEmployeeFinance_shouldDeleteAndReturnDto() {
        DeleteDto expectedDto = new DeleteDto();
        expectedDto.setUuid(financeUuid);

        when(employeeFinanceService.deleteEmployeeFinance(financeUuid)).thenReturn(financeUuid);

        when(employeeFinanceMapper.toDeletedDto(financeUuid)).thenReturn(expectedDto);

        DeleteDto result = employeeFinanceRestService.deleteEmployeeFinance(financeUuid);

        assertSame(expectedDto, result);

        verify(employeeFinanceService).deleteEmployeeFinance(financeUuid);

        verify(employeeFinanceMapper).toDeletedDto(financeUuid);
    }

    @Test
    void deleteEmployeeFinance_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeFinanceService.deleteEmployeeFinance(financeUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeFinanceRestService.deleteEmployeeFinance(financeUuid));

        assertSame(exception, thrown);

        verify(employeeFinanceService).deleteEmployeeFinance(financeUuid);

        verifyNoInteractions(employeeFinanceMapper);
    }

    @Test
    void deleteEmployeeFinance_shouldPropagateMapperException() {
        when(employeeFinanceService.deleteEmployeeFinance(financeUuid)).thenReturn(financeUuid);

        RuntimeException exception = new RuntimeException("Delete mapping failed");

        when(employeeFinanceMapper.toDeletedDto(financeUuid)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class, () -> employeeFinanceRestService.deleteEmployeeFinance(financeUuid));

        assertSame(exception, thrown);

        verify(employeeFinanceService).deleteEmployeeFinance(financeUuid);

        verify(employeeFinanceMapper).toDeletedDto(financeUuid);
    }
}
