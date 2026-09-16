package com.h4h.employeeportal.employee.restapi.api;

import com.h4h.employeeportal.employee.restapi.dto.ChangePasswordDto;
import com.h4h.employeeportal.shared.dto.DeleteDto;
import com.h4h.employeeportal.employee.restapi.dto.UserDto;
import com.h4h.employeeportal.employee.restapi.dto.create.CreateUserDto;
import com.h4h.employeeportal.employee.restapi.dto.update.UpdateUserDto;
import com.h4h.employeeportal.employee.restapi.restservice.UserRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/core/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserRestController {

    private final UserRestService userRestService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        UserDto createdUser = userRestService.createUser(createUserDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userRestService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable UUID id) {
        return userRestService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable UUID id, @RequestBody UpdateUserDto updateUserDto) {
        return userRestService.updateUser(id, updateUserDto);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @Valid @PathVariable UUID id, @RequestBody ChangePasswordDto changePasswordDto) {
        userRestService.updatePassword(id, changePasswordDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public DeleteDto deleteUser(@PathVariable UUID id) {
        return userRestService.deleteUser(id);
    }
}
