package com.ark.construction.entity;

import com.ark.construction.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BankAccount extends BaseEntity {

    private String bankName;

    private String accountHolder;

    private String accountNumber;

    private String ifscCode;

    private String branchName;

    private String upiId;

    private Boolean isDefault = false;
}