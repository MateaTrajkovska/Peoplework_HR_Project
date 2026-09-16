package com.h4h.employeeportal.shared;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdOn = OffsetDateTime.now();

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy = "hr-admin";

    @LastModifiedDate
    private OffsetDateTime modifiedOn;

    @LastModifiedBy
    private String modifiedBy;
}
