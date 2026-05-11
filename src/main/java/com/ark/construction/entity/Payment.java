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
public class Payment extends BaseEntity {

    private Double amount;

    private LocalDate paymentDate;

    private String paymentMode;

    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}