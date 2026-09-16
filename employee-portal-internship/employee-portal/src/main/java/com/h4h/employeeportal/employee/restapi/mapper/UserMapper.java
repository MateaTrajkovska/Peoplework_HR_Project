package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.model.User;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.UserDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateUserDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "uuid")
    UserDto toDto(User entity);

    @Mapping(target = "uuid", source = "id")
    User toEntity(UserDto dto);

    @Mapping(target = "uuid", ignore = true)
    User toEntityCreate(CreateUserDto createUserDto);

    @Mapping(target = "uuid", ignore = true)
    User toEntityUpdate(UpdateUserDto updateUserDto);

    DeleteDto toDeleteDto(UUID uuid);

    List<UserDto> toDtoList(List<User> users);
}
