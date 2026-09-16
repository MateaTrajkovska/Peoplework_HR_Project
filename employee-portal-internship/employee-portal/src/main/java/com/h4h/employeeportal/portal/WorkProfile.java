package com.h4h.employeeportal.portal;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "portal_work_profile")
public class WorkProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false, unique = true)
    public UUID employeeId;

    public UUID departmentId;

    public String jobTitle;

    public String location;

    public String workMode;

    public String avatarColor;
}