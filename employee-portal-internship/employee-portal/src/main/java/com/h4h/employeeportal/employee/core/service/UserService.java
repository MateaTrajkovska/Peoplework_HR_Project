package com.h4h.employeeportal.employee.core.service;

import com.h4h.employeeportal.employee.core.enumeration.LanguageEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserStatusEnum;
import com.h4h.employeeportal.employee.core.model.QUser;
import com.h4h.employeeportal.employee.core.model.User;
import com.h4h.employeeportal.employee.core.repository.UserRepository;
import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getLanguage() == null) {
            user.setLanguage(LanguageEnum.ENG);
        }
        if (user.getStatus() == null) {
            user.setStatus(UserStatusEnum.ACTIVE);
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID userUuid) {
        return userRepository.findById(userUuid).orElseThrow(() -> new ResourceNotFoundException("user.notFound"));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAllByStatus(UserStatusEnum.ACTIVE);
    }

    @Transactional
    public User updateUser(UUID userUuid, User updatedUser) {
        User existingUser = getUserById(userUuid);
        BeanUtils.copyProperties(updatedUser, existingUser, "uuid", "password");
        return userRepository.save(existingUser);
    }

    @Transactional
    public void updatePassword(UUID userUuid, String currentPassword, String newPassword) {
        User user = getUserById(userUuid);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ResourceNotFoundException("password.invalid");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public UUID deleteUser(UUID userUuid) {
        QUser user = QUser.user;

        long rowsUpdated = queryFactory
                .update(user)
                .where(user.uuid.eq(userUuid))
                .set(user.status, UserStatusEnum.ARCHIVED)
                .execute();

        if (rowsUpdated == 0) {
            throw new ResourceNotFoundException("user.notFound");
        }

        return userUuid;
    }
}
