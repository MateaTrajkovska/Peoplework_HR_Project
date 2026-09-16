package com.h4h.employeeportal.portal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "portal_contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    public UUID employeeId;

    @Column(nullable = false, unique = true)
    public String number;

    public String type;
    public String status;
    public LocalDate startDate;
    public LocalDate endDate;
    public BigDecimal salary;
    public String currency;

    @Column(length = 4000)
    public String notes;
}