package com.h4h.employeeportal.portal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "portal_activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    public String title;
    public String detail;
    public String kind;
    public UUID employeeId;
    public Instant occurredAt = Instant.now();
}