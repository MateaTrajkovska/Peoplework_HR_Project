package com.h4h.employeeportal.employee.restapi.mapper;

import com.h4h.employeeportal.employee.core.enumeration.LanguageEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserRoleEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserStatusEnum;
import com.h4h.employeeportal.employee.core.model.User;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.UserDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateUserDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private UUID userUuid;
    private User user;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
        userUuid = UUID.randomUUID();

        user = mockUser(
                userUuid, "john.doe", "password123", UserRoleEnum.ADMIN, UserStatusEnum.ACTIVE, LanguageEnum.ENG);
    }

    @Test
    void toDto_shouldMapAllFields() {
        UserDto result = userMapper.toDto(user);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userUuid, result.getId()),
                () -> assertEquals("john.doe", result.getUsername()),
                () -> assertEquals(UserRoleEnum.ADMIN, result.getRole()),
                () -> assertEquals(UserStatusEnum.ACTIVE, result.getStatus()),
                () -> assertEquals(LanguageEnum.ENG, result.getLanguage()));
    }

    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        assertNull(userMapper.toDto(null));
    }

    @Test
    void toDto_shouldMapNullValues() {
        User userWithNullValues = mockUser(userUuid, null, null, null, null, null);

        UserDto result = userMapper.toDto(userWithNullValues);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userUuid, result.getId()),
                () -> assertNull(result.getUsername()),
                () -> assertNull(result.getRole()),
                () -> assertNull(result.getStatus()),
                () -> assertNull(result.getLanguage()));
    }

    @Test
    void toEntity_shouldMapAllFields() {
        UserDto dto =
                mockUserDto(userUuid, "jane.doe", UserRoleEnum.GENERAL, UserStatusEnum.ARCHIVED, LanguageEnum.MKD);

        User result = userMapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userUuid, result.getUuid()),
                () -> assertEquals("jane.doe", result.getUsername()),
                () -> assertEquals(UserRoleEnum.GENERAL, result.getRole()),
                () -> assertEquals(UserStatusEnum.ARCHIVED, result.getStatus()),
                () -> assertEquals(LanguageEnum.MKD, result.getLanguage()));
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        assertNull(userMapper.toEntity(null));
    }

    @Test
    void toEntity_shouldMapNullValues() {
        User result = userMapper.toEntity(mockUserDto(null, null, null, null, null));

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getUsername()),
                () -> assertNull(result.getRole()),
                () -> assertNull(result.getStatus()),
                () -> assertNull(result.getLanguage()));
    }

    @Test
    void toEntityCreate_shouldMapAllFields() {
        CreateUserDto dto = mockCreateUserDto(UserStatusEnum.ACTIVE, LanguageEnum.ENG);

        User result = userMapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertEquals("john.doe", result.getUsername()),
                () -> assertEquals("password123", result.getPassword()),
                () -> assertEquals(UserRoleEnum.ADMIN, result.getRole()),
                () -> assertEquals(UserStatusEnum.ACTIVE, result.getStatus()),
                () -> assertEquals(LanguageEnum.ENG, result.getLanguage()));
    }

    @Test
    void toEntityCreate_shouldMapNullOptionalFields() {
        CreateUserDto dto = mockCreateUserDto(null, null);

        User result = userMapper.toEntityCreate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertEquals("john.doe", result.getUsername()),
                () -> assertEquals("password123", result.getPassword()),
                () -> assertEquals(UserRoleEnum.ADMIN, result.getRole()),
                () -> assertNull(result.getStatus()),
                () -> assertNull(result.getLanguage()));
    }

    @Test
    void toEntityCreate_shouldReturnNullWhenDtoIsNull() {
        assertNull(userMapper.toEntityCreate(null));
    }

    @Test
    void toEntityUpdate_shouldMapAllFields() {
        UpdateUserDto dto = mockUpdateUserDto(
                "updated.user", "newPassword123", UserRoleEnum.GENERAL, UserStatusEnum.ARCHIVED, LanguageEnum.MKD);

        User result = userMapper.toEntityUpdate(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertEquals("updated.user", result.getUsername()),
                () -> assertEquals("newPassword123", result.getPassword()),
                () -> assertEquals(UserRoleEnum.GENERAL, result.getRole()),
                () -> assertEquals(UserStatusEnum.ARCHIVED, result.getStatus()),
                () -> assertEquals(LanguageEnum.MKD, result.getLanguage()));
    }

    @Test
    void toEntityUpdate_shouldMapNullValues() {
        User result = userMapper.toEntityUpdate(mockUpdateUserDto(null, null, null, null, null));

        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.getUuid()),
                () -> assertNull(result.getUsername()),
                () -> assertNull(result.getPassword()),
                () -> assertNull(result.getRole()),
                () -> assertNull(result.getStatus()),
                () -> assertNull(result.getLanguage()));
    }

    @Test
    void toEntityUpdate_shouldReturnNullWhenDtoIsNull() {
        assertNull(userMapper.toEntityUpdate(null));
    }

    @Test
    void toDeleteDto_shouldSetUuid() {
        DeleteDto result = userMapper.toDeleteDto(userUuid);

        assertAll(() -> assertNotNull(result), () -> assertEquals(userUuid, result.getUuid()));
    }

    @Test
    void toDeleteDto_shouldReturnNullWhenUuidIsNull() {
        assertNull(userMapper.toDeleteDto(null));
    }

    @Test
    void toDtoList_shouldMapAllUsers() {
        UUID secondUuid = UUID.randomUUID();

        User secondUser = mockUser(
                secondUuid, "jane.doe", "password456", UserRoleEnum.GENERAL, UserStatusEnum.ARCHIVED, LanguageEnum.MKD);

        List<UserDto> result = userMapper.toDtoList(List.of(user, secondUser));

        assertEquals(2, result.size());

        assertUserDto(result.get(0), userUuid, "john.doe", UserRoleEnum.ADMIN, UserStatusEnum.ACTIVE, LanguageEnum.ENG);

        assertUserDto(
                result.get(1), secondUuid, "jane.doe", UserRoleEnum.GENERAL, UserStatusEnum.ARCHIVED, LanguageEnum.MKD);
    }

    @Test
    void toDtoList_shouldReturnEmptyListWhenInputListIsEmpty() {
        List<UserDto> result = userMapper.toDtoList(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnNullWhenInputListIsNull() {
        assertNull(userMapper.toDtoList(null));
    }

    private User mockUser(
            UUID uuid,
            String username,
            String password,
            UserRoleEnum role,
            UserStatusEnum status,
            LanguageEnum language) {

        return User.builder()
                .uuid(uuid)
                .username(username)
                .password(password)
                .role(role)
                .status(status)
                .language(language)
                .build();
    }

    private UserDto mockUserDto(
            UUID id, String username, UserRoleEnum role, UserStatusEnum status, LanguageEnum language) {

        return UserDto.builder()
                .id(id)
                .username(username)
                .role(role)
                .status(status)
                .language(language)
                .build();
    }

    private CreateUserDto mockCreateUserDto(UserStatusEnum status, LanguageEnum language) {

        return CreateUserDto.builder()
                .username("john.doe")
                .password("password123")
                .role(UserRoleEnum.ADMIN)
                .status(status)
                .language(language)
                .build();
    }

    private UpdateUserDto mockUpdateUserDto(
            String username, String password, UserRoleEnum role, UserStatusEnum status, LanguageEnum language) {

        return UpdateUserDto.builder()
                .username(username)
                .password(password)
                .role(role)
                .status(status)
                .language(language)
                .build();
    }

    private void assertUserDto(
            UserDto result, UUID id, String username, UserRoleEnum role, UserStatusEnum status, LanguageEnum language) {

        assertAll(
                () -> assertEquals(id, result.getId()),
                () -> assertEquals(username, result.getUsername()),
                () -> assertEquals(role, result.getRole()),
                () -> assertEquals(status, result.getStatus()),
                () -> assertEquals(language, result.getLanguage()));
    }
}
