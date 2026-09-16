package com.h4h.employeeportal.employee.restapi.restservice;

import com.h4h.employeeportal.employee.core.model.User;
import com.h4h.employeeportal.employee.core.service.UserService;
import com.h4h.employeeportal.employee.restapi.dto.ChangePasswordDto;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.UserDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateUserDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateUserDto;
import com.h4h.employeeportal.employee.restapi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRestService {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserDto createUser(CreateUserDto createUserDto) {
        User user = userService.createUser(userMapper.toEntityCreate(createUserDto));
        return userMapper.toDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userService.getAllUsers());
    }

    public UserDto getUserById(UUID userUuid) {
        return userMapper.toDto(userService.getUserById(userUuid));
    }

    public UserDto updateUser(UUID userUuid, UpdateUserDto updateUserDto) {
        User updatedUser = userService.updateUser(userUuid, userMapper.toEntityUpdate(updateUserDto));
        return userMapper.toDto(updatedUser);
    }

    public void updatePassword(UUID userUuid, ChangePasswordDto changePasswordDto) {
        userService.updatePassword(
                userUuid, changePasswordDto.getCurrentPassword(), changePasswordDto.getNewPassword());
    }

    public DeleteDto deleteUser(UUID userUuid) {
        return userMapper.toDeleteDto(userService.deleteUser(userUuid));
    }
}
