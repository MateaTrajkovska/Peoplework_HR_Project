package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.restservice.EmployeeInfoRestService;
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
class EmployeeInfoRestControllerTest {

    @Mock
    private EmployeeInfoRestService employeeInfoRestService;

    @InjectMocks
    private EmployeeInfoRestController employeeInfoRestController;

    private UUID employeeInfoUuid;

    @BeforeEach
    void setUp() {
        employeeInfoUuid = UUID.randomUUID();
    }

    @Test
    void createEmployeeInfo_shouldReturnCreatedInfo() {
        CreateEmployeeInfoDto createDto = new CreateEmployeeInfoDto();
        EmployeeInfoDto expectedDto = new EmployeeInfoDto();

        when(employeeInfoRestService.createEmployeeInfo(createDto)).thenReturn(expectedDto);

        EmployeeInfoDto result = employeeInfoRestController.createEmployeeInfo(createDto);

        assertSame(expectedDto, result);

        verify(employeeInfoRestService).createEmployeeInfo(createDto);
    }

    @Test
    void createEmployeeInfo_shouldPropagateServiceException() {
        CreateEmployeeInfoDto createDto = new CreateEmployeeInfoDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.info.notFound");

        when(employeeInfoRestService.createEmployeeInfo(createDto)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeInfoRestController.createEmployeeInfo(createDto));

        assertSame(exception, thrown);

        verify(employeeInfoRestService).createEmployeeInfo(createDto);
    }

    @Test
    void getAllEmployeeInfo_shouldReturnAllInfo() {
        EmployeeInfoDto firstDto = new EmployeeInfoDto();
        EmployeeInfoDto secondDto = new EmployeeInfoDto();

        List<EmployeeInfoDto> expected = List.of(firstDto, secondDto);

        when(employeeInfoRestService.getAllEmployeeInfo()).thenReturn(expected);

        List<EmployeeInfoDto> result = employeeInfoRestController.getAllEmployeeInfo();

        assertAll(() -> assertSame(expected, result), () -> assertEquals(2, result.size()));

        verify(employeeInfoRestService).getAllEmployeeInfo();
    }

    @Test
    void getAllEmployeeInfo_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.info.notFound");

        when(employeeInfoRestService.getAllEmployeeInfo()).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> employeeInfoRestController.getAllEmployeeInfo());

        assertSame(exception, thrown);

        verify(employeeInfoRestService).getAllEmployeeInfo();
    }

    @Test
    void getEmployeeInfoById_shouldReturnInfo() {
        EmployeeInfoDto expectedDto = new EmployeeInfoDto();

        when(employeeInfoRestService.getEmployeeInfoById(employeeInfoUuid)).thenReturn(expectedDto);

        EmployeeInfoDto result = employeeInfoRestController.getEmployeeInfoById(employeeInfoUuid);

        assertSame(expectedDto, result);

        verify(employeeInfoRestService).getEmployeeInfoById(employeeInfoUuid);
    }

    @Test
    void getEmployeeInfoById_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.info.notFound");

        when(employeeInfoRestService.getEmployeeInfoById(employeeInfoUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeInfoRestController.getEmployeeInfoById(employeeInfoUuid));

        assertSame(exception, thrown);

        verify(employeeInfoRestService).getEmployeeInfoById(employeeInfoUuid);
    }

    @Test
    void updateEmployeeInfo_shouldReturnUpdatedInfo() {
        UpdateEmployeeInfoDto updateDto = new UpdateEmployeeInfoDto();
        EmployeeInfoDto expectedDto = new EmployeeInfoDto();

        when(employeeInfoRestService.updateEmployeeInfo(employeeInfoUuid, updateDto))
                .thenReturn(expectedDto);

        EmployeeInfoDto result = employeeInfoRestController.updateEmployeeInfo(employeeInfoUuid, updateDto);

        assertSame(expectedDto, result);

        verify(employeeInfoRestService).updateEmployeeInfo(employeeInfoUuid, updateDto);
    }

    @Test
    void updateEmployeeInfo_shouldPropagateNotFoundException() {
        UpdateEmployeeInfoDto updateDto = new UpdateEmployeeInfoDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("employee.info.notFound");

        when(employeeInfoRestService.updateEmployeeInfo(employeeInfoUuid, updateDto))
                .thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeInfoRestController.updateEmployeeInfo(employeeInfoUuid, updateDto));

        assertSame(exception, thrown);

        verify(employeeInfoRestService).updateEmployeeInfo(employeeInfoUuid, updateDto);
    }

    @Test
    void deleteEmployeeInfoById_shouldReturnDeleteDto() {
        DeleteDto expectedDto = new DeleteDto();

        when(employeeInfoRestService.deleteEmployeeInfo(employeeInfoUuid)).thenReturn(expectedDto);

        DeleteDto result = employeeInfoRestController.deleteEmployeeInfoById(employeeInfoUuid);

        assertSame(expectedDto, result);

        verify(employeeInfoRestService).deleteEmployeeInfo(employeeInfoUuid);
    }

    @Test
    void deleteEmployeeInfoById_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("employee.info.notFound");

        when(employeeInfoRestService.deleteEmployeeInfo(employeeInfoUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeInfoRestController.deleteEmployeeInfoById(employeeInfoUuid));

        assertSame(exception, thrown);

        verify(employeeInfoRestService).deleteEmployeeInfo(employeeInfoUuid);
    }
}
