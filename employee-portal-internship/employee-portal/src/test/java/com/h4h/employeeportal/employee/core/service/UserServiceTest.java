package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.LanguageEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserRoleEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserStatusEnum;
import com.h4h.employeeportal.employee.core.model.QUser;
import com.h4h.employeeportal.employee.core.model.User;
import com.h4h.employeeportal.employee.core.repository.UserRepository;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAUpdateClause updateClause;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UUID userUuid;
    private User user;

    @BeforeEach
    void setUp() {
        userUuid = UUID.randomUUID();

        user = mockUser(
                userUuid, "john.doe", "encodedPassword", UserRoleEnum.GENERAL, UserStatusEnum.ACTIVE, LanguageEnum.ENG);
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

    private void mockDeleteQuery(long affectedRows) {
        when(queryFactory.update(QUser.user)).thenReturn(updateClause);

        when(updateClause.where(any(Predicate.class))).thenReturn(updateClause);

        when(updateClause.set(QUser.user.status, UserStatusEnum.ARCHIVED)).thenReturn(updateClause);

        when(updateClause.execute()).thenReturn(affectedRows);
    }

    private void verifyDeleteQuery() {
        verify(queryFactory).update(QUser.user);
        verify(updateClause).where(any(Predicate.class));
        verify(updateClause).set(QUser.user.status, UserStatusEnum.ARCHIVED);
        verify(updateClause).execute();
    }

    @Test
    void createUser_shouldEncodePasswordAndSaveUser() {
        User newUser = mockUser(
                null, "john.doe", "plainPassword", UserRoleEnum.GENERAL, UserStatusEnum.ACTIVE, LanguageEnum.ENG);

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = userService.createUser(newUser);

        assertAll(
                () -> assertSame(newUser, result),
                () -> assertEquals("encodedPassword", result.getPassword()),
                () -> assertEquals(UserStatusEnum.ACTIVE, result.getStatus()),
                () -> assertEquals(LanguageEnum.ENG, result.getLanguage()));

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(newUser);
    }

    @Test
    void createUser_shouldDefaultLanguageToEnglishWhenLanguageIsNull() {
        User newUser = mockUser(null, "john.doe", "plainPassword", UserRoleEnum.GENERAL, UserStatusEnum.ACTIVE, null);

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = userService.createUser(newUser);

        assertAll(
                () -> assertEquals("encodedPassword", result.getPassword()),
                () -> assertEquals(LanguageEnum.ENG, result.getLanguage()),
                () -> assertEquals(UserStatusEnum.ACTIVE, result.getStatus()));

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(newUser);
    }

    @Test
    void createUser_shouldDefaultStatusToActiveWhenStatusIsNull() {
        User newUser = mockUser(null, "john.doe", "plainPassword", UserRoleEnum.GENERAL, null, LanguageEnum.MKD);

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = userService.createUser(newUser);

        assertAll(
                () -> assertEquals("encodedPassword", result.getPassword()),
                () -> assertEquals(LanguageEnum.MKD, result.getLanguage()),
                () -> assertEquals(UserStatusEnum.ACTIVE, result.getStatus()));

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(newUser);
    }

    @Test
    void createUser_shouldApplyBothDefaultValuesWhenLanguageAndStatusAreNull() {
        User newUser = mockUser(null, "john.doe", "plainPassword", UserRoleEnum.GENERAL, null, null);

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = userService.createUser(newUser);

        assertAll(
                () -> assertEquals("encodedPassword", result.getPassword()),
                () -> assertEquals(LanguageEnum.ENG, result.getLanguage()),
                () -> assertEquals(UserStatusEnum.ACTIVE, result.getStatus()));

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(newUser);
    }

    @Test
    void createUser_shouldPreserveMacedonianLanguageWhenProvided() {
        User newUser = mockUser(
                null, "john.doe", "plainPassword", UserRoleEnum.GENERAL, UserStatusEnum.ACTIVE, LanguageEnum.MKD);

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = userService.createUser(newUser);

        assertAll(
                () -> assertEquals(LanguageEnum.MKD, result.getLanguage()),
                () -> assertEquals(UserStatusEnum.ACTIVE, result.getStatus()));

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(newUser);
    }

    @Test
    void createUser_shouldPreserveArchivedStatusWhenProvided() {
        User newUser = mockUser(
                null, "john.doe", "plainPassword", UserRoleEnum.ADMIN, UserStatusEnum.ARCHIVED, LanguageEnum.ENG);

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = userService.createUser(newUser);

        assertAll(
                () -> assertEquals(UserStatusEnum.ARCHIVED, result.getStatus()),
                () -> assertEquals(LanguageEnum.ENG, result.getLanguage()));

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(newUser);
    }

    @Test
    void getUserById_shouldReturnUserWhenFound() {
        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userUuid);

        assertSame(user, result);

        verify(userRepository).findById(userUuid);
    }

    @Test
    void getUserById_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userUuid)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userUuid));

        assertEquals("user.notFound", exception.getMessage());

        verify(userRepository).findById(userUuid);
    }

    @Test
    void getAllUsers_shouldReturnActiveUsers() {
        User secondUser = mockUser(
                UUID.randomUUID(),
                "jane.smith",
                "encodedPassword2",
                UserRoleEnum.ADMIN,
                UserStatusEnum.ACTIVE,
                LanguageEnum.MKD);

        List<User> users = List.of(user, secondUser);

        when(userRepository.findAllByStatus(UserStatusEnum.ACTIVE)).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertAll(() -> assertEquals(users, result), () -> assertEquals(2, result.size()));

        verify(userRepository).findAllByStatus(UserStatusEnum.ACTIVE);
    }

    @Test
    void getAllUsers_shouldReturnEmptyListWhenNoActiveUsersExist() {
        when(userRepository.findAllByStatus(UserStatusEnum.ACTIVE)).thenReturn(List.of());

        List<User> result = userService.getAllUsers();

        assertAll(() -> assertNotNull(result), () -> assertTrue(result.isEmpty()));

        verify(userRepository).findAllByStatus(UserStatusEnum.ACTIVE);
    }

    @Test
    void updateUser_shouldUpdateUserPropertiesAndPreserveUuidAndPassword() {
        String originalPassword = user.getPassword();

        User updatedUser = mockUser(
                UUID.randomUUID(),
                "updated.username",
                "newPassword",
                UserRoleEnum.ADMIN,
                UserStatusEnum.ARCHIVED,
                LanguageEnum.MKD);

        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(userUuid, updatedUser);

        assertAll(
                () -> assertSame(user, result),
                () -> assertEquals(userUuid, result.getUuid()),
                () -> assertEquals(originalPassword, result.getPassword()),
                () -> assertEquals("updated.username", result.getUsername()),
                () -> assertEquals(UserRoleEnum.ADMIN, result.getRole()),
                () -> assertEquals(UserStatusEnum.ARCHIVED, result.getStatus()),
                () -> assertEquals(LanguageEnum.MKD, result.getLanguage()));

        verify(userRepository).findById(userUuid);
        verify(userRepository).save(user);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void updateUser_shouldPreservePasswordWhenUpdatedPasswordIsProvided() {
        String originalPassword = user.getPassword();

        User updatedUser = mockUser(
                UUID.randomUUID(),
                "updated.username",
                "attemptedNewPassword",
                UserRoleEnum.ADMIN,
                UserStatusEnum.ACTIVE,
                LanguageEnum.ENG);

        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(userUuid, updatedUser);

        assertAll(
                () -> assertEquals(originalPassword, result.getPassword()),
                () -> assertNotEquals("attemptedNewPassword", result.getPassword()));

        verify(userRepository).findById(userUuid);
        verify(userRepository).save(user);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void updateUser_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userUuid)).thenReturn(Optional.empty());

        User updatedUser = mockUser(
                UUID.randomUUID(),
                "updated.username",
                "newPassword",
                UserRoleEnum.ADMIN,
                UserStatusEnum.ACTIVE,
                LanguageEnum.MKD);

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userUuid, updatedUser));

        assertEquals("user.notFound", exception.getMessage());

        verify(userRepository).findById(userUuid);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updatePassword_shouldEncodeAndSaveNewPasswordWhenCurrentPasswordIsCorrect() {
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String encodedNewPassword = "encodedNewPassword";

        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        userService.updatePassword(userUuid, currentPassword, newPassword);

        assertEquals(encodedNewPassword, user.getPassword());

        verify(userRepository).findById(userUuid);
        verify(passwordEncoder).matches(currentPassword, "encodedPassword");
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(user);
    }

    @Test
    void updatePassword_shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        String currentPassword = "wrongPassword";
        String newPassword = "newPassword";

        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updatePassword(userUuid, currentPassword, newPassword));

        assertEquals("password.invalid", exception.getMessage());

        verify(userRepository).findById(userUuid);
        verify(passwordEncoder).matches(currentPassword, "encodedPassword");
        verify(passwordEncoder, never()).encode(newPassword);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updatePassword_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userUuid)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updatePassword(userUuid, "currentPassword", "newPassword"));

        assertEquals("user.notFound", exception.getMessage());

        verify(userRepository).findById(userUuid);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updatePassword_shouldEncodeNewPasswordOnlyAfterCurrentPasswordIsVerified() {
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";

        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);

        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        userService.updatePassword(userUuid, currentPassword, newPassword);

        var inOrder = inOrder(passwordEncoder, userRepository);

        inOrder.verify(passwordEncoder).matches(currentPassword, "encodedPassword");

        inOrder.verify(passwordEncoder).encode(newPassword);

        inOrder.verify(userRepository).save(user);
    }

    @Test
    void deleteUser_shouldReturnUuidWhenUserExists() {
        mockDeleteQuery(1L);

        UUID result = userService.deleteUser(userUuid);

        assertEquals(userUuid, result);

        verifyDeleteQuery();
    }

    @Test
    void deleteUser_shouldThrowExceptionWhenNoRowsUpdated() {
        mockDeleteQuery(0L);

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userUuid));

        assertEquals("user.notFound", exception.getMessage());

        verifyDeleteQuery();
    }

    @Test
    void deleteUser_shouldReturnUuidWhenMultipleRowsAreUpdated() {
        mockDeleteQuery(2L);

        UUID result = userService.deleteUser(userUuid);

        assertEquals(userUuid, result);

        verifyDeleteQuery();
    }
}
