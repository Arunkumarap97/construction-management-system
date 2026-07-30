package com.ark.construction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class PaymentRequest extends BaseEntity{

    private Double amount;
    private String remarks;
    private LocalDate requestDate;

    private String status = "PENDING";

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    @Transient
    private Long projectId;

    @Transient
    private Long bankAccountId;

}
