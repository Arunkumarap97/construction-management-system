package com.ark.construction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class AdminAccount extends BaseEntity {

    private String type; // INCOME / EXPENSE

    private Double amount;

    private LocalDate entryDate;

    private String description;

}
