package com.ark.construction.entity;

import com.ark.construction.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Project extends BaseEntity {

    private String projectName;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double totalCost;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status = "PLANNING";

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