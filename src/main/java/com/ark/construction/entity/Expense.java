package com.ark.construction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class Expense extends BaseEntity {

    private LocalDate expenseDate;
    private String category;
    private String vendorName;
    private Double amount;
    private String note;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    // helper field for form dropdown
    @Transient
    private Long projectId;

}
