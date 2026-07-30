package com.ark.construction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class Project extends BaseEntity {

    private String projectName;

    private String location;

    private Double totalCost;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;

    private Integer progressPercent = 0;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Transient
    private Long clientId;

    @OneToMany(mappedBy = "project")
    private List<Payment> payments;

    @OneToMany(mappedBy = "project")
    private List<Expense> expenses;
}