package com.ark.construction.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID guid;

    @Column(nullable = false)
    private Boolean active = true;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        if (guid == null) {
            guid = UUID.randomUUID();
        }

        if (active == null) {
            active = true;
        }

        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}