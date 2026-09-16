package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.User;
import com.h4h.employeeportal.employee.core.service.UserService;
import com.h4h.employeeportal.employee.restapi.dto.ChangePasswordDto;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.UserDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateUserDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateUserDto;
import com.h4h.employeeportal.employee.restapi.mapper.UserMapper;
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
class UserRestServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserRestService userRestService;

    private UUID userUuid;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userUuid = UUID.randomUUID();

        user = mock(User.class);
        userDto = mock(UserDto.class);
    }

    private ResourceNotFoundException userNotFoundException() {
        return new ResourceNotFoundException("user.notFound");
    }

    @Test
    void createUser_shouldCreateAndReturnDto() {
        CreateUserDto createUserDto = new CreateUserDto();

        User mappedUser = mock(User.class);
        UserDto expectedDto = mock(UserDto.class);

        when(userMapper.toEntityCreate(createUserDto)).thenReturn(mappedUser);
        when(userService.createUser(mappedUser)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expectedDto);

        UserDto result = userRestService.createUser(createUserDto);

        assertSame(expectedDto, result);

        verify(userMapper).toEntityCreate(createUserDto);
        verify(userService).createUser(mappedUser);
        verify(userMapper).toDto(user);
    }

    @Test
    void createUser_shouldReturnNullWhenMapperReturnsNullDto() {
        CreateUserDto createUserDto = new CreateUserDto();

        when(userMapper.toEntityCreate(createUserDto)).thenReturn(user);
        when(userService.createUser(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(null);

        UserDto result = userRestService.createUser(createUserDto);

        assertNull(result);

        verify(userMapper).toEntityCreate(createUserDto);
        verify(userService).createUser(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void createUser_shouldPropagateServiceException() {
        CreateUserDto createUserDto = new CreateUserDto();
        RuntimeException exception = new RuntimeException("Database error");

        when(userMapper.toEntityCreate(createUserDto)).thenReturn(user);
        when(userService.createUser(user)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> userRestService.createUser(createUserDto));

        assertSame(exception, thrown);

        verify(userMapper).toEntityCreate(createUserDto);
        verify(userService).createUser(user);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void getAllUsers_shouldReturnMappedUsers() {
        User secondUser = mock(User.class);
        UserDto secondUserDto = mock(UserDto.class);

        List<User> users = List.of(user, secondUser);
        List<UserDto> expectedDtos = List.of(userDto, secondUserDto);

        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(expectedDtos);

        List<UserDto> result = userRestService.getAllUsers();

        assertEquals(expectedDtos, result);

        verify(userService).getAllUsers();
        verify(userMapper).toDtoList(users);
    }

    @Test
    void getAllUsers_shouldReturnEmptyListWhenNoUsersExist() {
        List<User> users = List.of();
        List<UserDto> expectedDtos = List.of();

        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(expectedDtos);

        List<UserDto> result = userRestService.getAllUsers();

        assertTrue(result.isEmpty());

        verify(userService).getAllUsers();
        verify(userMapper).toDtoList(users);
    }

    @Test
    void getAllUsers_shouldReturnNullWhenMapperReturnsNull() {
        List<User> users = List.of(user);

        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(null);

        List<UserDto> result = userRestService.getAllUsers();

        assertNull(result);

        verify(userService).getAllUsers();
        verify(userMapper).toDtoList(users);
    }

    @Test
    void getAllUsers_shouldPropagateServiceException() {
        ResourceNotFoundException exception = userNotFoundException();

        when(userService.getAllUsers()).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> userRestService.getAllUsers());

        assertSame(exception, thrown);

        verify(userService).getAllUsers();
        verifyNoInteractions(userMapper);
    }

    @Test
    void getUserById_shouldReturnMappedDto() {
        when(userService.getUserById(userUuid)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userRestService.getUserById(userUuid);

        assertSame(userDto, result);

        verify(userService).getUserById(userUuid);
        verify(userMapper).toDto(user);
    }

    @Test
    void getUserById_shouldReturnNullWhenMapperReturnsNull() {
        when(userService.getUserById(userUuid)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(null);

        UserDto result = userRestService.getUserById(userUuid);

        assertNull(result);

        verify(userService).getUserById(userUuid);
        verify(userMapper).toDto(user);
    }

    @Test
    void getUserById_shouldPropagateResourceNotFoundException() {
        ResourceNotFoundException exception = userNotFoundException();

        when(userService.getUserById(userUuid)).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> userRestService.getUserById(userUuid));

        assertSame(exception, thrown);

        verify(userService).getUserById(userUuid);
        verifyNoInteractions(userMapper);
    }

    @Test
    void updateUser_shouldUpdateAndReturnDto() {
        UpdateUserDto updateUserDto = new UpdateUserDto();

        User mappedUser = mock(User.class);
        User updatedUser = mock(User.class);
        UserDto expectedDto = mock(UserDto.class);

        when(userMapper.toEntityUpdate(updateUserDto)).thenReturn(mappedUser);
        when(userService.updateUser(userUuid, mappedUser)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(expectedDto);

        UserDto result = userRestService.updateUser(userUuid, updateUserDto);

        assertSame(expectedDto, result);

        verify(userMapper).toEntityUpdate(updateUserDto);
        verify(userService).updateUser(userUuid, mappedUser);
        verify(userMapper).toDto(updatedUser);
    }

    @Test
    void updateUser_shouldReturnNullWhenMapperReturnsNullDto() {
        UpdateUserDto updateUserDto = new UpdateUserDto();

        when(userMapper.toEntityUpdate(updateUserDto)).thenReturn(user);
        when(userService.updateUser(userUuid, user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(null);

        UserDto result = userRestService.updateUser(userUuid, updateUserDto);

        assertNull(result);

        verify(userMapper).toEntityUpdate(updateUserDto);
        verify(userService).updateUser(userUuid, user);
        verify(userMapper).toDto(user);
    }

    @Test
    void updateUser_shouldPropagateServiceException() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        RuntimeException exception = new RuntimeException("Update failed");

        when(userMapper.toEntityUpdate(updateUserDto)).thenReturn(user);
        when(userService.updateUser(userUuid, user)).thenThrow(exception);

        RuntimeException thrown =
                assertThrows(RuntimeException.class, () -> userRestService.updateUser(userUuid, updateUserDto));

        assertSame(exception, thrown);

        verify(userMapper).toEntityUpdate(updateUserDto);
        verify(userService).updateUser(userUuid, user);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void updatePassword_shouldPassPasswordsToService() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setCurrentPassword("old-password");
        changePasswordDto.setNewPassword("new-password");

        assertDoesNotThrow(() -> userRestService.updatePassword(userUuid, changePasswordDto));

        verify(userService).updatePassword(userUuid, "old-password", "new-password");

        verifyNoInteractions(userMapper);
    }

    @Test
    void updatePassword_shouldPropagateServiceException() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setCurrentPassword("wrong-password");
        changePasswordDto.setNewPassword("new-password");

        ResourceNotFoundException exception = new ResourceNotFoundException("password.invalid");

        doThrow(exception).when(userService).updatePassword(userUuid, "wrong-password", "new-password");

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> userRestService.updatePassword(userUuid, changePasswordDto));

        assertSame(exception, thrown);

        verify(userService).updatePassword(userUuid, "wrong-password", "new-password");

        verifyNoInteractions(userMapper);
    }

    @Test
    void deleteUser_shouldDeleteAndReturnDeleteDto() {
        DeleteDto expectedDto = new DeleteDto();
        expectedDto.setUuid(userUuid);

        when(userService.deleteUser(userUuid)).thenReturn(userUuid);
        when(userMapper.toDeleteDto(userUuid)).thenReturn(expectedDto);

        DeleteDto result = userRestService.deleteUser(userUuid);

        assertSame(expectedDto, result);

        verify(userService).deleteUser(userUuid);
        verify(userMapper).toDeleteDto(userUuid);
    }

    @Test
    void deleteUser_shouldReturnNullWhenMapperReturnsNull() {
        when(userService.deleteUser(userUuid)).thenReturn(userUuid);
        when(userMapper.toDeleteDto(userUuid)).thenReturn(null);

        DeleteDto result = userRestService.deleteUser(userUuid);

        assertNull(result);

        verify(userService).deleteUser(userUuid);
        verify(userMapper).toDeleteDto(userUuid);
    }

    @Test
    void deleteUser_shouldPropagateServiceException() {
        ResourceNotFoundException exception = userNotFoundException();

        when(userService.deleteUser(userUuid)).thenThrow(exception);

        ResourceNotFoundException thrown =
                assertThrows(ResourceNotFoundException.class, () -> userRestService.deleteUser(userUuid));

        assertSame(exception, thrown);

        verify(userService).deleteUser(userUuid);
        verifyNoInteractions(userMapper);
    }
}
