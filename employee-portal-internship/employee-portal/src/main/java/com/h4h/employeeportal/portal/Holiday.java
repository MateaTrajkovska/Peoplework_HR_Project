package com.h4h.employeeportal.portal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "portal_holiday")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, unique = true)
    public LocalDate date;

    @Column(nullable = false)
    public String name;
}