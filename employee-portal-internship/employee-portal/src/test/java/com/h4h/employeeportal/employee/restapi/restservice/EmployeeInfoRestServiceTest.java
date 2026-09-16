package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.EmployeeInfo;
import com.h4h.employeeportal.employee.core.service.EmployeeInfoService;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.EmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateEmployeeInfoDto;
import com.h4h.employeeportal.employee.restapi.mapper.EmployeeInfoMapper;
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
class EmployeeInfoRestServiceTest {

    @Mock
    private EmployeeInfoService employeeInfoService;

    @Mock
    private EmployeeInfoMapper employeeInfoMapper;

    @InjectMocks
    private EmployeeInfoRestService employeeInfoRestService;

    private UUID infoUuid;

    private EmployeeInfo employeeInfo;
    private EmployeeInfo mappedEmployeeInfo;
    private EmployeeInfoDto employeeInfoDto;
    private CreateEmployeeInfoDto createEmployeeInfoDto;
    private UpdateEmployeeInfoDto updateEmployeeInfoDto;

    @BeforeEach
    void setUp() {
        infoUuid = UUID.randomUUID();

        employeeInfo = mock(EmployeeInfo.class);
        mappedEmployeeInfo = mock(EmployeeInfo.class);
        employeeInfoDto = mock(EmployeeInfoDto.class);
        createEmployeeInfoDto = mock(CreateEmployeeInfoDto.class);
        updateEmployeeInfoDto = mock(UpdateEmployeeInfoDto.class);
    }

    private ResourceNotFoundException notFoundException() {
        return new ResourceNotFoundException("employee.info.notFound");
    }

    @Test
    void createEmployeeInfo_shouldMapSaveAndReturnDto() {
        when(employeeInfoMapper.toEntityCreate(createEmployeeInfoDto)).thenReturn(mappedEmployeeInfo);

        when(employeeInfoService.createEmployeeInfo(mappedEmployeeInfo)).thenReturn(employeeInfo);

        when(employeeInfoMapper.toDto(employeeInfo)).thenReturn(employeeInfoDto);

        EmployeeInfoDto result = employeeInfoRestService.createEmployeeInfo(createEmployeeInfoDto);

        assertAll(() -> assertNotNull(result), () -> assertSame(employeeInfoDto, result));

        verify(employeeInfoMapper).toEntityCreate(createEmployeeInfoDto);

        verify(employeeInfoService).createEmployeeInfo(mappedEmployeeInfo);

        verify(employeeInfoMapper).toDto(employeeInfo);
    }

    @Test
    void createEmployeeInfo_shouldReturnNullWhenServiceReturnsNull() {
        when(employeeInfoMapper.toEntityCreate(createEmployeeInfoDto)).thenReturn(mappedEmployeeInfo);

        when(employeeInfoService.createEmployeeInfo(mappedEmployeeInfo)).thenReturn(null);

        when(employeeInfoMapper.toDto(null)).thenReturn(null);

        EmployeeInfoDto result = employeeInfoRestService.createEmployeeInfo(createEmployeeInfoDto);

        assertNull(result);

        verify(employeeInfoMapper).toEntityCreate(createEmployeeInfoDto);

        verify(employeeInfoService).createEmployeeInfo(mappedEmployeeInfo);

        verify(employeeInfoMapper).toDto(null);
    }

    @Test
    void createEmployeeInfo_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Mapping failed");

        when(employeeInfoMapper.toEntityCreate(createEmployeeInfoDto)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class, () -> employeeInfoRestService.createEmployeeInfo(createEmployeeInfoDto));

        assertSame(exception, thrown);

        verify(employeeInfoMapper).toEntityCreate(createEmployeeInfoDto);

        verifyNoInteractions(employeeInfoService);
    }

    @Test
    void createEmployeeInfo_shouldPropagateServiceException() {
        when(employeeInfoMapper.toEntityCreate(createEmployeeInfoDto)).thenReturn(mappedEmployeeInfo);

        RuntimeException exception = new RuntimeException("Database error");

        when(employeeInfoService.createEmployeeInfo(mappedEmployeeInfo)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class, () -> employeeInfoRestService.createEmployeeInfo(createEmployeeInfoDto));

        assertSame(exception, thrown);

        verify(employeeInfoMapper).toEntityCreate(createEmployeeInfoDto);

        verify(employeeInfoService).createEmployeeInfo(mappedEmployeeInfo);

        verify(employeeInfoMapper, never()).toDto(any());
    }

    @Test
    void getAllEmployeeInfo_shouldReturnMappedList() {
        EmployeeInfo secondEmployeeInfo = mock(EmployeeInfo.class);
        EmployeeInfoDto secondEmployeeInfoDto = mock(EmployeeInfoDto.class);

        List<EmployeeInfo> employeeInfos = List.of(employeeInfo, secondEmployeeInfo);

        List<EmployeeInfoDto> expectedDtos = List.of(employeeInfoDto, secondEmployeeInfoDto);

        when(employeeInfoService.getAllEmployeeInfo()).thenReturn(employeeInfos);

        when(employeeInfoMapper.toDtoList(employeeInfos)).thenReturn(expectedDtos);

        List<EmployeeInfoDto> result = employeeInfoRestService.getAllEmployeeInfo();

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertSame(expectedDtos, result));

        verify(employeeInfoService).getAllEmployeeInfo();

        verify(employeeInfoMapper).toDtoList(employeeInfos);
    }

    @Test
    void getAllEmployeeInfo_shouldReturnEmptyList() {
        List<EmployeeInfo> employeeInfos = List.of();
        List<EmployeeInfoDto> expectedDtos = List.of();

        when(employeeInfoService.getAllEmployeeInfo()).thenReturn(employeeInfos);

        when(employeeInfoMapper.toDtoList(employeeInfos)).thenReturn(expectedDtos);

        List<EmployeeInfoDto> result = employeeInfoRestService.getAllEmployeeInfo();

        assertTrue(result.isEmpty());

        verify(employeeInfoService).getAllEmployeeInfo();

        verify(employeeInfoMapper).toDtoList(employeeInfos);
    }

    @Test
    void getAllEmployeeInfo_shouldPropagateServiceException() {
        RuntimeException exception = new RuntimeException("Database error");

        when(employeeInfoService.getAllEmployeeInfo()).thenThrow(exception);

        RuntimeException thrown =
                assertThrows(RuntimeException.class, () -> employeeInfoRestService.getAllEmployeeInfo());

        assertSame(exception, thrown);

        verify(employeeInfoService).getAllEmployeeInfo();

        verifyNoInteractions(employeeInfoMapper);
    }

    @Test
    void getEmployeeInfoById_shouldReturnMappedDto() {
        when(employeeInfoService.getEmployeeInfoById(infoUuid)).thenReturn(employeeInfo);

        when(employeeInfoMapper.toDto(employeeInfo)).thenReturn(employeeInfoDto);

        EmployeeInfoDto result = employeeInfoRestService.getEmployeeInfoById(infoUuid);

        assertAll(() -> assertNotNull(result), () -> assertSame(employeeInfoDto, result));

        verify(employeeInfoService).getEmployeeInfoById(infoUuid);

        verify(employeeInfoMapper).toDto(employeeInfo);
    }

    @Test
    void getEmployeeInfoById_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeInfoService.getEmployeeInfoById(infoUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeInfoRestService.getEmployeeInfoById(infoUuid));

        assertSame(exception, thrown);

        verify(employeeInfoService).getEmployeeInfoById(infoUuid);

        verifyNoInteractions(employeeInfoMapper);
    }

    @Test
    void updateEmployeeInfo_shouldMapUpdateAndReturnDto() {
        when(employeeInfoMapper.toEntityUpdate(updateEmployeeInfoDto)).thenReturn(mappedEmployeeInfo);

        when(employeeInfoService.updateEmployeeInfo(infoUuid, mappedEmployeeInfo))
                .thenReturn(employeeInfo);

        when(employeeInfoMapper.toDto(employeeInfo)).thenReturn(employeeInfoDto);

        EmployeeInfoDto result = employeeInfoRestService.updateEmployeeInfo(infoUuid, updateEmployeeInfoDto);

        assertAll(() -> assertNotNull(result), () -> assertSame(employeeInfoDto, result));

        verify(employeeInfoMapper).toEntityUpdate(updateEmployeeInfoDto);

        verify(employeeInfoService).updateEmployeeInfo(infoUuid, mappedEmployeeInfo);

        verify(employeeInfoMapper).toDto(employeeInfo);
    }

    @Test
    void updateEmployeeInfo_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeInfoMapper.toEntityUpdate(updateEmployeeInfoDto)).thenReturn(mappedEmployeeInfo);

        when(employeeInfoService.updateEmployeeInfo(infoUuid, mappedEmployeeInfo))
                .thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeInfoRestService.updateEmployeeInfo(infoUuid, updateEmployeeInfoDto));

        assertSame(exception, thrown);

        verify(employeeInfoMapper).toEntityUpdate(updateEmployeeInfoDto);

        verify(employeeInfoService).updateEmployeeInfo(infoUuid, mappedEmployeeInfo);

        verify(employeeInfoMapper, never()).toDto(any());
    }

    @Test
    void updateEmployeeInfo_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Mapping failed");

        when(employeeInfoMapper.toEntityUpdate(updateEmployeeInfoDto)).thenThrow(exception);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> employeeInfoRestService.updateEmployeeInfo(infoUuid, updateEmployeeInfoDto));

        assertSame(exception, thrown);

        verify(employeeInfoMapper).toEntityUpdate(updateEmployeeInfoDto);

        verifyNoInteractions(employeeInfoService);
    }

    @Test
    void updateEmployeeInfo_shouldNotChangeUuidOrEmployeeUuidInMappedEntity() {
        EmployeeInfo mappedEntity = new EmployeeInfo();

        when(employeeInfoMapper.toEntityUpdate(updateEmployeeInfoDto)).thenReturn(mappedEntity);

        when(employeeInfoService.updateEmployeeInfo(infoUuid, mappedEntity)).thenReturn(employeeInfo);

        when(employeeInfoMapper.toDto(employeeInfo)).thenReturn(employeeInfoDto);

        EmployeeInfoDto result = employeeInfoRestService.updateEmployeeInfo(infoUuid, updateEmployeeInfoDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(mappedEntity.getUuid()),
                () -> assertNull(mappedEntity.getEmployeeUuid()));

        verify(employeeInfoMapper).toEntityUpdate(updateEmployeeInfoDto);

        verify(employeeInfoService).updateEmployeeInfo(infoUuid, mappedEntity);

        verify(employeeInfoMapper).toDto(employeeInfo);
    }

    @Test
    void deleteEmployeeInfo_shouldDeleteAndReturnDto() {
        DeleteDto expectedDto = new DeleteDto();
        expectedDto.setUuid(infoUuid);

        when(employeeInfoService.deleteEmployeeInfoById(infoUuid)).thenReturn(infoUuid);

        when(employeeInfoMapper.toDeletedDto(infoUuid)).thenReturn(expectedDto);

        DeleteDto result = employeeInfoRestService.deleteEmployeeInfo(infoUuid);

        assertAll(
                () -> assertNotNull(result),
                () -> assertSame(expectedDto, result),
                () -> assertEquals(infoUuid, result.getUuid()));

        verify(employeeInfoService).deleteEmployeeInfoById(infoUuid);

        verify(employeeInfoMapper).toDeletedDto(infoUuid);
    }

    @Test
    void deleteEmployeeInfo_shouldPropagateNotFoundException() {
        ResourceNotFoundException exception = notFoundException();

        when(employeeInfoService.deleteEmployeeInfoById(infoUuid)).thenThrow(exception);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> employeeInfoRestService.deleteEmployeeInfo(infoUuid));

        assertSame(exception, thrown);

        verify(employeeInfoService).deleteEmployeeInfoById(infoUuid);

        verifyNoInteractions(employeeInfoMapper);
    }

    @Test
    void deleteEmployeeInfo_shouldPropagateMapperException() {
        RuntimeException exception = new RuntimeException("Delete mapping failed");

        when(employeeInfoService.deleteEmployeeInfoById(infoUuid)).thenReturn(infoUuid);

        when(employeeInfoMapper.toDeletedDto(infoUuid)).thenThrow(exception);

        RuntimeException thrown =
                assertThrows(RuntimeException.class, () -> employeeInfoRestService.deleteEmployeeInfo(infoUuid));

        assertSame(exception, thrown);

        verify(employeeInfoService).deleteEmployeeInfoById(infoUuid);

        verify(employeeInfoMapper).toDeletedDto(infoUuid);
    }
}
