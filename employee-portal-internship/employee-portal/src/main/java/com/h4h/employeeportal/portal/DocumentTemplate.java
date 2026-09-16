package com.h4h.employeeportal.portal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "portal_document_template")
public class DocumentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    public String name;

    public String category;

    @Column(length = 12000)
    public String body;
}