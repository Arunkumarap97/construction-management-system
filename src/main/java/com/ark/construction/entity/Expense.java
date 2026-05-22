package com.ark.construction.entity;

import com.ark.construction.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Expense extends BaseEntity {

    private LocalDate expenseDate;

    private String category;

    private String vendorName;

    private Double amount;

    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Transient
    private Long projectId;

    @Column(nullable = false)
    private Boolean active = true;
}