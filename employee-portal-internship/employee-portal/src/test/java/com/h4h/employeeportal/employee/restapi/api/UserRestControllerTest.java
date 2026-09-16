package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.employee.restapi.dto.ChangePasswordDto;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.UserDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateUserDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateUserDto;
import com.h4h.employeeportal.employee.restapi.restservice.UserRestService;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    private UserRestService userRestService;

    @InjectMocks
    private UserRestController userRestController;

    private UUID userUuid;

    @BeforeEach
    void setUp() {
        userUuid = UUID.randomUUID();
    }

    @Test
    void createUser_shouldReturnCreatedUserWithCreatedStatus() {
        CreateUserDto createDto = new CreateUserDto();
        UserDto expectedDto = new UserDto();

        when(userRestService.createUser(createDto)).thenReturn(expectedDto);

        ResponseEntity<UserDto> result = userRestController.createUser(createDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertSame(expectedDto, result.getBody());

        verify(userRestService).createUser(createDto);
    }

    @Test
    void createUser_shouldReturnCreatedStatusWhenServiceReturnsNull() {
        CreateUserDto createDto = new CreateUserDto();

        when(userRestService.createUser(createDto)).thenReturn(null);

        ResponseEntity<UserDto> result = userRestController.createUser(createDto);
        assertAll(() -> assertEquals(HttpStatus.CREATED, result.getStatusCode()), () -> assertNull(result.getBody()));

        verify(userRestService).createUser(createDto);
    }

    @Test
    void createUser_shouldPropagateServiceException() {
        CreateUserDto createDto = new CreateUserDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("user.notFound");

        when(userRestService.createUser(createDto)).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> userRestController.createUser(createDto));

        assertSame(exception, thrown);

        verify(userRestService).createUser(createDto);
    }

    @Test
    void getAllUsers_shouldReturnUsers() {
        List<UserDto> expectedUsers = List.of(new UserDto(), new UserDto());

        when(userRestService.getAllUsers()).thenReturn(expectedUsers);

        List<UserDto> result = userRestController.getAllUsers();

        assertSame(expectedUsers, result);

        verify(userRestService).getAllUsers();
    }

    @Test
    void getAllUsers_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("user.notFound");

        when(userRestService.getAllUsers()).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> userRestController.getAllUsers());

        assertSame(exception, thrown);

        verify(userRestService).getAllUsers();
    }

    @Test
    void getUserById_shouldReturnUser() {
        UserDto expectedDto = new UserDto();

        when(userRestService.getUserById(userUuid)).thenReturn(expectedDto);

        UserDto result = userRestController.getUserById(userUuid);

        assertSame(expectedDto, result);

        verify(userRestService).getUserById(userUuid);
    }

    @Test
    void getUserById_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("user.notFound");

        when(userRestService.getUserById(userUuid)).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> userRestController.getUserById(userUuid));

        assertSame(exception, thrown);

        verify(userRestService).getUserById(userUuid);
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        UpdateUserDto updateDto = new UpdateUserDto();
        UserDto expectedDto = new UserDto();

        when(userRestService.updateUser(userUuid, updateDto)).thenReturn(expectedDto);

        UserDto result = userRestController.updateUser(userUuid, updateDto);

        assertSame(expectedDto, result);

        verify(userRestService).updateUser(userUuid, updateDto);
    }

    @Test
    void updateUser_shouldPropagateServiceException() {
        UpdateUserDto updateDto = new UpdateUserDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("user.notFound");

        when(userRestService.updateUser(userUuid, updateDto)).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> userRestController.updateUser(userUuid, updateDto));

        assertSame(exception, thrown);

        verify(userRestService).updateUser(userUuid, updateDto);
    }

    @Test
    void updatePassword_shouldReturnNoContent() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();

        doNothing().when(userRestService).updatePassword(userUuid, changePasswordDto);

        ResponseEntity<Void> result = userRestController.updatePassword(userUuid, changePasswordDto);

        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode()), () -> assertNull(result.getBody()));

        verify(userRestService).updatePassword(userUuid, changePasswordDto);
    }

    @Test
    void updatePassword_shouldPropagateServiceException() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();

        ResourceNotFoundException exception = new ResourceNotFoundException("user.notFound");

        doThrow(exception).when(userRestService).updatePassword(userUuid, changePasswordDto);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> userRestController.updatePassword(userUuid, changePasswordDto));

        assertSame(exception, thrown);

        verify(userRestService).updatePassword(userUuid, changePasswordDto);
    }

    @Test
    void deleteUser_shouldReturnDeleteDto() {
        DeleteDto expectedDto = new DeleteDto();

        when(userRestService.deleteUser(userUuid)).thenReturn(expectedDto);

        DeleteDto result = userRestController.deleteUser(userUuid);

        assertSame(expectedDto, result);

        verify(userRestService).deleteUser(userUuid);
    }

    @Test
    void deleteUser_shouldPropagateServiceException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("user.notFound");

        when(userRestService.deleteUser(userUuid)).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> userRestController.deleteUser(userUuid));

        assertSame(exception, thrown);

        verify(userRestService).deleteUser(userUuid);
    }
}
