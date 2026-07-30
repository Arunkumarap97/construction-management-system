package com.ark.construction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class Payment extends BaseEntity {

    private Double amount;
    private LocalDate paymentDate;
    private String paymentMode;
    private String note;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

}
