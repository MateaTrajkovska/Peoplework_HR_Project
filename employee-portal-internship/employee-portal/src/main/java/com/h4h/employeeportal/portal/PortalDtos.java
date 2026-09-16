package com.h4h.employeeportal.portal;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PortalDtos {

 public record EmployeeInput(
         @NotBlank @Size(max = 80) String firstName,
         @NotBlank @Size(max = 80) String lastName,
         @NotBlank @Email String email,
         @NotBlank String phone,
         String personalId,
         @NotBlank String nationality,
         @NotNull LocalDate dateOfBirth,
         @NotNull LocalDate startDate,
         String gender,
         String employeeType,
         String degree,
         @NotBlank String jobTitle,
         @NotNull UUID departmentId,
         @NotBlank String municipality,
         String street,
         String postalCode,
         String location,
         String workMode,
         @Pattern(regexp = "[0-9]{0,18}") String bankAccount,
         @Size(max = 1000) String notes
 ) {}

 public record ContractInput(
         @NotNull UUID employeeId,
         @NotBlank String number,
         @NotBlank String type,
         @NotNull LocalDate startDate,
         LocalDate endDate,
         @NotNull @DecimalMin("0") BigDecimal salary,
         @NotBlank String currency,
         @Size(max = 4000) String notes
 ) {}

 public record LeaveInput(
         @NotNull UUID employeeId,
         @NotNull LocalDate fromDate,
         @NotNull LocalDate toDate,
         @NotBlank String type,
         @Size(max = 1000) String reason
 ) {}

 public record Decision(
         @NotBlank String status
 ) {}

 public record DepartmentInput(
         @NotBlank @Size(max = 100) String name,
         @Size(max = 500) String description
 ) {}

 public record TemplateInput(
         @NotBlank @Size(max = 150) String name,
         @NotBlank String category,
         @NotBlank @Size(max = 12000) String body
 ) {}

 public record HolidayInput(
         @NotNull LocalDate date,
         @NotBlank String name
 ) {}

 public record PlanInput(
         @NotNull UUID employeeId,
         @Min(2020) @Max(2100) int year,
         @Min(0) @Max(100) int maxDays
 ) {}

 public record Termination(
         @NotNull LocalDate date,
         @NotBlank @Size(max = 1000) String reason
 ) {}
}