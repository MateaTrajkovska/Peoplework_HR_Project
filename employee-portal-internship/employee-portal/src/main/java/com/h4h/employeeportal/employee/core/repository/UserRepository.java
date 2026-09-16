package com.h4h.employeeportal.employee.core.repository;

import com.h4h.employeeportal.employee.core.enumeration.UserStatusEnum;
import com.h4h.employeeportal.employee.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findAllByStatus(UserStatusEnum status);
}
